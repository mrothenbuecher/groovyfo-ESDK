/*
* ow1/GROOVYFO.PARALLEL.TEST
*
*/

def parallel = new Parallel(this);

// parallel.addGroovyFO("ow1/GROOVYFO.BENCHMARK");
parallel.addGroovyFO("ow1/GROOVYFO.TEST");
parallel.addGroovyFO("ow1/GROOVYFO.UNITTEST");

println "Start"

try{
    def results = parallel.execute();
}catch(Exception ex){
    println "es ist ein Fehler aufgetretten "+ex
}finally{
    println "durchgefhrt"
    
    println "Ergebnisse: "+results.size();
    
    for(def result: results){
            if(result.get().getMessages().size() > 0){
                for(def msg : result.get().getMessages()){
                    println msg
                } 
            }
            if(result.get().hasExecption()){
                    result.get().getException().printStacktrace();
            }
    }
    
    println "fertig"
}