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
import org.codehaus.groovy.runtime.metaclass.ConcurrentReaderHashMap;
import org.codehaus.groovy.runtime.metaclass.MetaClassRegistryImpl;

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
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 * 
 *         JFOP erwartet als ersten Parameter das Groovyscript welches
 *         ausgeführt werden soll und übergibt alle Parameter an dieses weiter
 * 
 */
public class ScriptExecutor implements ContextRunnable {

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	
	public static String[] findPackageNamesStartingWith(String prefix) {
		Package[] packages = Package.getPackages();
		List<String> list = new ArrayList<String>();
		for(Package pack:packages) {
			if(pack.getName().startsWith(prefix)) {
				//System.out.println("DEBUG: "+pack.getName());
				list.add(pack.getName());
			}
		}
	    return list.toArray(new String[0]);
	}
	
	// private static GroovyShell shell;

	private static GroovyShell getShell(Binding binding) {
		GroovyShell shell;
		// Imports festlegen damit diese nicht selbst
		// hinzugefügt werden müssen
		CompilerConfiguration cc = new CompilerConfiguration();

		ImportCustomizer ic = new ImportCustomizer();
		// abas Standard
		ic.addStarImports("de.abas.eks.jfop.remote");
		// ic.addStaticStars("de.abas.eks.jfop.remote.EKS");
		// Helferklassen für GroovyFO
		ic.addStarImports("de.finetech.groovy");
		ic.addStarImports("de.finetech.groovy.utils");
		ic.addStarImports("de.finetech.groovy.utils.datatypes");
		ic.addStarImports("de.finetech.utils", "de.finetech.utils.charts");
		ic.addImports("java.awt.Color", "java.util.Calendar", "java.text.SimpleDateFormat", "java.text.DateFormat",
				"java.util.Date");

		// alle abas db Packages importieren
		ic.addStarImports(ScriptExecutor.findPackageNamesStartingWith("de.abas.erp.db"));
		
		cc.addCompilationCustomizers(ic);
		// Basisklasse festlegen
		cc.setScriptBaseClass("de.finetech.groovy.AbasBaseScript");

		// Script ausführen
		GroovyClassLoader loader = new GroovyClassLoader();
		shell = new GroovyShell(loader, binding, cc);

		return shell;
	}

	public static int executeScript(FOPSessionContext arg0, String[] arg1) {
		GroovyShell shell = null;
		boolean error = false;

		EKS.eingabe("DATEI.F");
		// Genug Parameter übergben?
		if (arg1.length > 1) {
			File groovyScript = new File(arg1[1]);
			// existiert die Datei ?
			if (groovyScript.exists()) {
				// ist es eine Datei ?
				if (groovyScript.isFile()) {
					Object o = null;
					Script gscript = null;
					try {

						Binding binding = new Binding();
						// Parameter weitergeben
						binding.setVariable("arg0", arg0);
						binding.setVariable("args", arg1);
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
						// FIXME Sprach unabhüngigkeit
						FO.box("Fehler", e.getMessage());
						FO.box("Unbehandelte Ausnahme in " + arg1[1], getStacktrace(e));
						error = true;
					} catch (AbortedException e) {
						// FIXME Sprach unabhüngigkeit
						FO.box("FOP abgebrochen", "FOP wurde durch Anwender abgebrochen");
						error = true;
					} catch (CompilationFailedException e) {
						// FIXME Sprach unabhüngigkeit
						// FO.box("übersetzung fehlgeschlagen", "GroovyFO konnte
						// das Script nicht übersetzen: "
						// + e.getMessage() + "\n" + getStacktrace(e));
						FO.box("übersetzung fehlgeschlagen: ", getStacktrace(e));
						error = true;
					} catch (Exception e) {
						// FIXME Sprach unabhüngigkeit
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
							// FIXME müglichkeit für büsen Fehler
							return -1;
						}
					}
				} else {
					// FIXME Sprach unabhüngigkeit
					FO.box("Unzureichende Argumente", "Groovy Script ist keine Datei!");
					return -1;
				}
			} else {
				// FIXME Sprach unabhüngigkeit
				FO.box("Unzureichende Argumente", "Groovy Script existiert nicht!");
				return -1;
			}
		} else {
			// FIXME Sprach unabhüngigkeit
			FO.box("Unzureichende Argumente", "keine Groovy Script angegeben!");
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