package de.finetech.groovy;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.runtime.StackTraceUtils;
import org.codehaus.groovy.runtime.metaclass.ConcurrentReaderHashMap;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import de.abas.eks.jfop.AbortedException;
import de.abas.eks.jfop.CommandException;
import de.abas.eks.jfop.FOPException;
import de.abas.eks.jfop.remote.ContextRunnable;
import de.abas.eks.jfop.remote.EKS;
import de.abas.eks.jfop.remote.FO;
import de.abas.eks.jfop.remote.FOPSessionContext;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.lang.GroovySystem;
import groovy.lang.Script;

/**
 * @author Michael RothenbÅcher, Finetech GmbH & Co. KG
 * 
 *         JFOP erwartet als ersten Parameter das Groovyscript welches
 *         ausgefÅhrt werden soll und Åbergibt alle Parameter an dieses weiter
 * 
 */
public class ScriptExecutor implements ContextRunnable {
	
	private static final Logger log = LoggerFactory.getLogger(ScriptExecutor.class);
	
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static String[] findPackageNamesStartingWith(String prefix) {
		Package[] packages = Package.getPackages();
		List<String> list = new ArrayList<String>();
		for (Package pack : packages) {
			if (pack.getName().startsWith(prefix)) {
				// System.out.println("DEBUG: "+pack.getName());
				list.add(pack.getName());
			}
		}
		return list.toArray(new String[0]);
	}

	// private static GroovyShell shell;

	private static GroovyShell getShell(Binding binding) {
		GroovyShell shell;
		// Imports festlegen damit diese nicht selbst
		// hinzugefÑgt werden mÑssen
		CompilerConfiguration cc = new CompilerConfiguration();

		ImportCustomizer ic = new ImportCustomizer();
		// abas Standard
		ic.addStarImports("de.abas.eks.jfop.remote");
		// ic.addStaticStars("de.abas.eks.jfop.remote.EKS");
		// Helferklassen fÅr GroovyFO
		ic.addStarImports("de.finetech.groovy");
		ic.addStarImports("de.finetech.groovy.utils");
		ic.addStarImports("de.finetech.groovy.utils.datatypes");
		ic.addStarImports("de.finetech.utils", "de.finetech.utils.charts","de.finetech.utils.parallel");
		ic.addImports("java.awt.Color", "java.util.Calendar", "java.text.SimpleDateFormat", "java.text.DateFormat",
				"java.util.Date");

		// alle abas db Packages importieren
		ic.addStarImports(ScriptExecutor.findPackageNamesStartingWith("de.abas.erp.db"));
		
		cc.addCompilationCustomizers(ic);
		// Basisklasse festlegen
		cc.setScriptBaseClass("de.finetech.groovy.AbasBaseScript");

		// Script ausfÅhren
		GroovyClassLoader loader = new GroovyClassLoader();
		shell = new GroovyShell(loader, binding, cc);

		return shell;
	}

	public static int executeScript(FOPSessionContext arg0, String[] arg1) {
		GroovyShell shell = null;
		boolean error = false;
		
		EKS.eingabe("DATEI.F");
		// Genug Parameter Ñbergben?
		if (arg1.length > 1) {
			File groovyScript = new File(arg1[1]);
			// existiert die Datei ?
			if (groovyScript.exists()) {
				// ist es eine Datei ?
				if (groovyScript.isFile()) {
					MDC.put("SCRIPT", arg1[1]);
					Object o = null;
					Script gscript = null;
					try {

						Binding binding = new Binding();
						// Parameter weitergeben
						binding.setVariable("arg0", arg0);
						binding.setVariable("args", arg1);
						binding.setVariable("scriptfile", arg1[1]);
						binding.setVariable("dbContext", arg0.getDbContext());
						
						boolean debug = false;
						for (String key : arg1) {
							if (key.equals("GROOVYFODEBUG")) {
								debug = true;
								break;
							}
						}
						binding.setVariable("GROOVYFODEBUG", debug);
						shell = getShell(binding); 
						gscript = shell.parse(ScriptExecutor.readFile(arg1[1], Charset.forName("UTF-8")).intern());
						o = gscript.run();
						error = false;

					} catch (CommandException e) {
						// FIXME Sprach unabhÑngigkeit
						FO.fehler("Unbehandelte Ausnahme in " + arg1[1]+"("+GroovySystem.getVersion()+"/"+AbasBaseScript.version+"):\n"+ getStacktrace(e));
						log.error("Unbehandelte Ausnahme in " + arg1[1]+"("+GroovySystem.getVersion()+"/"+AbasBaseScript.version+"):\n", StackTraceUtils.deepSanitize(e));
						error = true;
					} catch (AbortedException e) {
						// FIXME Sprach unabhÑngigkeit
						FO.fehler("FOP("+arg1[1]+") abgebrochen \nFOP wurde durch Anwender abgebrochen");
						log.error("FOP("+arg1[1]+") abgebrochen \nFOP wurde durch Anwender abgebrochen");
						error = true;
					} catch (CompilationFailedException e) {
						// FIXME Sprach unabhÑngigkeit
						// FO.box("Ñbersetzung fehlgeschlagen", "GroovyFO konnte
						// das Script nicht Ñbersetzen: "
						// + e.getMessage() + "\n" + getStacktrace(e));
						FO.fehler("öbersetzung fehlgeschlagen ("+arg1[1]+"):\n"+ getStacktrace(e));
						log.error("öbersetzung fehlgeschlagen ("+arg1[1]+"):\n", StackTraceUtils.deepSanitize(e));
						error = true;
					} catch (Exception e) {
						// FIXME Sprach unabhÑngigkeit
						// FO.box("Unbehandelte Ausnahme in " + arg1[1],
						// getStacktrace(e));
						error = true;
						throw new FOPException(e.getMessage(), e);
					} finally {
						clearGroovyClassesCache();
						if (o != null)
							GroovySystem.getMetaClassRegistry().removeMetaClass(o.getClass());
						if (gscript != null)
							GroovySystem.getMetaClassRegistry().removeMetaClass(gscript.getClass());
						if (shell != null && shell.getClassLoader() != null)
							shell.getClassLoader().clearCache();
						// shell = null;
						if (error) {
							// FIXME mîglichkeit fÅr bîsen Fehler
							return -1;
						}
					}
				} else {
					// FIXME Sprach unabhÑngigkeit
					FO.fehler("Unzureichende Argumente\nGroovy Script "+arg1[1]+" ist keine Datei!");
					log.error("Unzureichende Argumente\nGroovy Script "+arg1[1]+" ist keine Datei!");
					return -1;
				}
			} else {
				// FIXME Sprach unabhÑngigkeit
				FO.fehler("Unzureichende Argumente\nGroovy Script "+arg1[1]+" existiert nicht!");
				log.error("Unzureichende Argumente\nGroovy Script "+arg1[1]+" existiert nicht!");
				return -1;
			}
		} else {
			// FIXME Sprach unabhÑngigkeit
			FO.fehler("Unzureichende Argumente\nkeine Groovy Script angegeben!");
			log.error("Unzureichende Argumente\\nkeine Groovy Script angegeben!", arg1);
			return -1;
		}
		return 0;
	}

	@Override
	public int runFop(FOPSessionContext arg0, String[] arg1) throws FOPException {
		return ScriptExecutor.executeScript(arg0, arg1);
	}

	private static String getStacktrace(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	protected static boolean clearGroovyClassesCache() {
		try {
			Class<MetaClassRegistryImpl> mcriClass = MetaClassRegistryImpl.class;

			Field constantMetaClassesField = mcriClass.getDeclaredField("constantMetaClasses");
			constantMetaClassesField.setAccessible(true);

			Field constantMetaClassCountField = mcriClass.getDeclaredField("constantMetaClassCount");
			constantMetaClassCountField.setAccessible(true);

			Class<GroovySystem> gsClass = GroovySystem.class;
			Field metaClassRegistryField = gsClass.getDeclaredField("META_CLASS_REGISTRY");
			metaClassRegistryField.setAccessible(true);
			MetaClassRegistryImpl mcri = (MetaClassRegistryImpl) metaClassRegistryField.get(null);
			ConcurrentReaderHashMap constantMetaClasses = (ConcurrentReaderHashMap) constantMetaClassesField.get(mcri);

			constantMetaClasses.clear();
			constantMetaClassCountField.set(mcri, 0);

			return true;
		} catch (Exception e) {
			return false;
		}
	}
}