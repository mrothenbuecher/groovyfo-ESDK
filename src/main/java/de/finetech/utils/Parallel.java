package de.finetech.utils;

import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import de.abas.eks.jfop.annotation.RunFop;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.DbMessage;
import de.abas.erp.db.EditorAction;
import de.abas.erp.db.EditorCommand;
import de.abas.erp.db.EditorCommandFactory;
import de.abas.erp.db.EditorObject;
import de.abas.erp.db.MessageListener;
import de.abas.erp.db.settings.DisplayMode;
import de.finetech.groovy.AbasBaseScript;
import de.finetech.utils.parallel.ParallelResult;

/**
 * execute multplie FOP's at the sametime (async)
 * 
 * default timeout is 30 sec.
 * 
 * @author Rothenbücher
 *
 */
public class Parallel {

	private AbasBaseScript script;
	private Vector<String[]> fops;
	private long timeout;
	
	public Parallel(AbasBaseScript script) {
		this.script = script;
		this.fops = new Vector<String[]>();
		this.timeout = 30;
	}
	
	/**
	 * 
	 * @param fop - e.g. ow1/EXECUTE.ME
	 */
	public Parallel addFOP(String fop) {
		this.fops.add(new String[]{fop,""});
		return this;
	}
	
	/**
	 * 
	 * @param groovyfo - e.g ow1/GROOVYFO.EXECUTE.ME
	 * @return
	 */
	public Parallel addGroovyFO(String groovyfo) {
		this.fops.add(new String[]{"java:GroovyFO@grvfo",groovyfo});
		return this;
	}
	
	/**
	 * default timeout is 30 sec.
	 * 
	 * @param timeout - seconds till timeout
	 */
	public Parallel setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}
	
	public List<Future<ParallelResult>> execute() throws InterruptedException {
		ExecutorService serv = Executors.newFixedThreadPool(8);
		List<Future<ParallelResult>> futures = null;
		Vector<Callable<ParallelResult>> tasks = new Vector<Callable<ParallelResult>>();
		for(String[] fop:fops) {
			tasks.add(new Callable<ParallelResult>() {
	
				@Override
				public ParallelResult call() throws Exception {
					ParallelResult result = new ParallelResult(fop[1].isEmpty()?fop[0]:fop[1]);
					//script.getFOPSessionContext().newClientDbContext(arg0)
					DbContext context = script.getFOPSessionContext().newClientDbContext("Foo "+fop[0]+" "+fop[1]);
					context.addMessageListener(new MessageListener() {

						@Override
						public void receiveMessage(DbMessage arg0) {
							result.addMessage(arg0);
							script.println(arg0.toString());
						}
						
					});
					
					context.getSettings().setDisplayMode(DisplayMode.OUTPUT);
					EditorObject obj = null;
					try {
						EditorCommand cmd = EditorCommandFactory.runIS("10169");
						//context.openInfosystem(RunFop.class)
						obj = context.openEditor(cmd);
						obj.setString("kommando", fop[0]+" "+fop[1]);
						obj.invokeButton("start");
					}catch(Exception e) {
						result.setException(e);
						script.println(e.getMessage());
					}finally {
						if(obj != null && obj.active()) {
							obj.close();
						}
						if(context.isActive())
							context.close();
					}
					
					//InfosystemcallResult isResult = Infosystemcall.build("10169").setHeadParameter("kommando", fop[0]+" "+fop[1]).setHeadParameter("bstart", "").execute();
					//isResult.
					return result;
				}
				
			});
		}
		futures = serv.invokeAll(tasks, this.timeout, TimeUnit.SECONDS);
		serv.shutdown();
		serv.awaitTermination(this.timeout, TimeUnit.SECONDS);
		//serv.awaitTermination(this.timeout, TimeUnit.SECONDS);
		
		return futures;
	}
	
}
