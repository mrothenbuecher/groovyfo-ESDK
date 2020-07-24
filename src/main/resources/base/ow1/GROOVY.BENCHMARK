println GroovySystem.version

println AbasBaseScript.version

art("I5", "xi", "xj")

for(def foo = 0;foo<10;foo++){
    
    def start = System.currentTimeMillis()
    def j = 0
    def avg = 0.0;
    for(int k=0;k<10;k++){
            start = System.currentTimeMillis();
            for(int i=0; i < 10000 ; i++){
                    j = i;
            }
            avg += (System.currentTimeMillis()-start)
    }
    println "Dauer groovy pur: "+(avg/10.0)+" ms"
    
    avg = 0.0
    u.xj = 0
    u.xi = 0
    for(int k=0;k<10;k++){
            start = System.currentTimeMillis();
            for(int i=0; i < 10000 ; i++){
                    u.xj = i
            }
            avg += (System.currentTimeMillis()-start)
    }
    println "Dauer Wertzuweisung an abas: "+(avg/10.0)+" ms"
    
    avg = 0.0
    u.xj = 0
    u.xi = 0
    
    for(int k=0;k<10;k++){
            start = System.currentTimeMillis();
            for(int i = 0; i < 10000 ; i++){
                    j = u.xi
            }
            avg += (System.currentTimeMillis()-start)
    }
    println "Dauer Wertauslesen aus abas: "+(avg/10.0)+" ms"
    
    avg = 0.0
    u.xj = 0
    u.xi = 0
    
    for(int k=0;k<10;k++){
            start = System.currentTimeMillis();
            for(int i = 0; i < 10000 ; i++){
                    u.xj = u.xi
            }
            avg += (System.currentTimeMillis()-start)
    }
    println "Dauer Wertauslesen aus abas und schreiben an abas: "+(avg/10.0)+" ms"
    
}