Status:**beta**

ESDK App basierend auf [GroovyFO](https://github.com/mrothenbuecher/GroovyFO)

GroovyFO
===

GroovyFO ist der Versuch das gute aus JFOP und Standard FO des abas-ERP zu kombinieren.

GroovyFO vereinfacht den Umgang mit JFOP durch das bereitstellen eines einfachen an FOP angelehnten Syntaxes.

Es können nun ohne zusätzliches kompilieren und redeployen JFOP's in Form von Groovyscripten ausgeführt werden. Außerdem sind Konstrukte wie while, for, if ..., Funktionen sowie Klassen möglich.

einige Vorteile:<br>
* [automatische Typkonvertierung](https://github.com/mrothenbuecher/GroovyFO/wiki/automatische-Typkonvertierung),beim Zugriff auf abas Variablen (bsp.: I3 -> Integer)
    * Auch Umgang mit abas Datum möglich
        *  `M.datum = M.datum +1`, einen Tag weiter
        *  `M.datum = M.datum -1`, einen Tag zurück
        *  `M.datum = M.datum & 1`
        *  `M.datum = M.datum % 1`
* Syntax an Standard FO angelehnt
    * Wertzuweisung `M.datum = M.datum +1`
* Zugriff mittels Dachoperator `H.platz^id`
* Kontrollstrukturen aus Java
    * `for`, `while`, `if`, `switch`, Methoden, Klassen ...
* Syntax vereinfachung gegenüber JFOP
* kein Redeployen nach Änderungen an GroovyFO-Scripten nötig, änderungen an den Scripten sind sofort wirksam
* diverse Helferklassen für Selektion, Infosystemaufrufe, Charts usw.
* Ablaufsteuerung durch die Methoden `always` und `onerror`

[Übersicht zur API](https://github.com/mrothenbuecher/GroovyFO/wiki/API)


## Funktionsweise

Die Klasse de.finetech.groovy.ScriptExecutor ist ein JFOP welches als ersten Parameter eine Textdatei (Groovyscript) erwartet. Dieses wird dann auf Basis der Script-Klasse de.finetech.groovy.AbasBaseScript initialisiert. Diese Klasse kapselt JFOP-Funktionen um so Schreibarbeit sparen zu können. Weiterhin besteht durch die Verwendung von Groovy die Möglichkeit Kontrollstrukturen wie if-Anweisungen, Schleifen oder eben auch Klassen im Kontext eines FOP zu verwenden. Dadurch das bei jeden Aufruf des ScriptExecuters die Groovy Datei neu interpretiert wird, können Änderungen an dieser Datei ohne erneutes redeployen des JFOP-Server wirksam werden. Dieses Verhalten ist ähnlich dem Verhalten von FOP's.


## Verwendung
Aufruf eines GroovyFO aus der Kommandoübersicht
```
<Text>java:GroovyFO@grvfo GROOVYSCRIPT_WELCHES_AUSGEFÜHRT_WERDEN_SOLL<zeigen>
```
oder in einem Infosystemen hinterlegen
```
java:GroovyFO@grvfo GROOVYSCRIPT_WELCHES_AUSGEFÜHRT_WERDEN_SOLL
```

## Beispiel GroovyFO

Text ow1/GROOVYFO.TEST wie folgt anlegen.
```groovy
/*
* Beispiel GroovyFO holt die ersten 100 Teile aus der DB
* und gibt deren Suchwort aus
* ow1/GROOVYFO.TEST
*/

for(def i=0; i<100 && hole Teil; i++){
        // Ausgabe auf Konsole
        println ( h.such )
}
```
Zum testen Kommando aufrufen mit
```
<Text>java:GroovyFO@grvfo ow1/GROOVYFO.TEST<zeigen>
```

#### Kurzübersicht
| JFOP                          | dt                                                                    | engl      |
| ----------------------------- | --------------------------------------------------------------------- | ----------|
| EKS.Hvar(...)                 | h.von                                                                 |           |
| EKS.Mvar(...)                 | m.von                                                                 |           |
| EKS.hole(...)                 | hole(...) <br/> hole(String db, SelectBuilder builder) <br/> hole(String db, String selektion)| select(...) <br/> select(String db, SelectBuilder builder) <br/> select(String db, String selektion)|
| EKS.lade(...)                 | lade(...) <br/> lade(int puffer, String db, SelectBuilder builder) <br/> lade(int puffer, String db, String selektion)| load(...) <br/> load(int puffer, String db, SelectBuilder builder) <br/> load(int puffer, String db, String selektion)|
| EKS.formel(...)               | fo(String variable, wert)                                             | |
| EKS.getValue(puffer, varName) | l1.von ... <br/> l2.von ... <br/> usw...                              | |
| EKS.println(...)              | println(...)                                                          | |
| EKS.box(...,...)              | box(...,...)                                                          | |
| EKS.eingabe(...)              | ein(...)                                                              | in(...)|
| EKS.bringe(...)               | bringe(...)                                                           | rewrite(...)|
| EKS.mache(...)                | mache(...)                                                            | make()|
| EKS.mache("maske zeile +O")   | plusZeile()                                                           | addRow()|
| EKS.bringe("maske zeile -O")  | entfZeile()                                                           | removeRow()|
| EKS.Dvar(...)                 | d.von                                                                 | |
|                               | mehr                                                                  | success or more|
|...                            | ...                                                                   | ... |

### Ablaufsteuerung

In jedem GroovyFO gibt es die Möglichkeit die Methoden `onerror(Exception ex)` und `always()` zu überschreiben.

`onerror` wird immer dann ausgeführt, solange sich das GroovyFO übersetzen lässt und wenn eine unbehandelte Ausnahme im Code des GroovyFO auftritt.

`always` wird, solange sich das GroovyFO übersetzen lässt, immer am Ende ausgeführt. <br>

```groovy

def onerror(def ex){
        // was ist zutun wenn ein Fehler beim ausführen des GroovyFO auftritt?
}

def always(){
        // am ende des GroovyFO, was soll immer gemacht werden?
        // diese Methode wird auch nach einem Fehler ausgeführt
}

```

### Helfer Klassen

#### [REST-Client](https://http-builder-ng.github.io/http-builder-ng/asciidoc/html5/)
```groovy
//let's configure an http client to make calls to httpbin.org using the default http library
def httpBin = HttpBuilder.configure {
    request.uri = 'http://httpbin.org/'
}

//now let's GET /get endpoint at httpbin.
//This will return a JSON formatted response with an origin property.
def result = httpBin.get {
    request.uri.path = '/get'
}
    
println("Your ip address is: ${result.origin}")
```


#### SelectionBuilder
Der [SelectionBuilder](https://github.com/mrothenbuecher/GroovyFO/wiki/SelectionBuilder) ist eine Hilfsklasse um einfach Selektion definieren zu können.
```groovy
// Artikel von a bis b
def selection1 = SelectionBuilder().normal("such2","A","B").database(2).group(1)
// im Matchcode auf den Namen in Bediensprache
def selection2 = SelectionBuilder().matchcode("namebspr","Schif*fahrt")
...
```

#### Infosystemcall
Die Klasse Infosystemcall ruft mittels edpinfosys.sh Infosystem auf und stellt das Ergebnis des Aufrufes
in Variablen bereit
```groovy
def result = Infosystemcall.build("VKZENTRALE")
        .addTableOutputField("ttrans")
        .addTableOutputField("tdate")
        .addTableOutputField("tkuli")
        .addTableOutputField("tklname")
        .addTableOutputField("teprice")
        .setHeadParameter("vktyp", "Rechnung")
        .setHeadParameter("ablageart", "abgelegt")
        .setHeadParameter("ynurger", "ja")
        .setHeadParameter("datef", "-30")
        .setHeadParameter("datet", ".")
        .setHeadParameter("bstart", "1")
        .execute()

for(def row: result.table){
        println row.ttrans
        println row.tdate
        println row.tkuli
        println row.tklname
        println row.teprice
}
```
#### ChartGenerator

Eine Klasse zum erzeugen von Charts
```groovy
// Liniendiagramm erzeugen
ChartGenerator gen = ChartGenerator.create(ChartType.LINES);
// Titel setzen
gen.setChartTitle("Hello World");
gen.setMarkerx("first", "second", "third", "fourth", "fifth")
gen.setAnglex(45)


// neue Datenserie definieren
DataSeries series = new DataSeries();
series.setTitle("Series 1");
series.addValue(Value.create(1));
series.addValue(Value.create(2));
series.addValue(Value.create(3));
series.addValue(Value.create(4));
series.addValue(Value.create(5));

// weiter Serie
DataSeries series2 = new DataSeries();
series2.setTitle("Series 2");
series2.addValue(Value.create(5));
series2.addValue(Value.create(4));
series2.addValue(Value.create(3));
series2.addValue(Value.create(2));
series2.addValue(Value.create(1));

gen.setOptions(true)

// neue Datenserie dem Chart bekannt machen
gen.addDataseries(series);
gen.addDataseries(series2);

// eigentliche generien des Charts im abas
// ychart ist im Kopfbereich ein Chartfeld
gen.generate("ychart");
```
Ergebnis:

![Chart](https://raw.githubusercontent.com/mrothenbuecher/GroovyFO/master/img/chart.jpg)