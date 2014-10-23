# Fragen

## 1. Was passiert, wenn Knotennamen mehrfach aufteten?
Da der Knotenname eine eindeutige Identifikation ist, werden beide Knoten als ein und der Selbe geachtet. Dies ist auch notig, da in dem gegebenen Format nur eine Kante pro Zeile und somit auch immer nur eine Kante pro Schritt eingelesen werden kann. Wenn nun zwei Knoten mit gleichem Namen als Unterschiedliche Knoten gehandhabt werden, würde ein falscher Graph, oder eher nicht der Graph, der gewünscht war, entstehen.

## 2. Wie unterscheidet sich der BFS für gerichtete und ungerichtete Graphen?
Bei gerichteten Graphen muss geprüft werden, ob die adjazenten Knoten auch erreichbar sind.

## 3. Wie können sie testen, dass ihre Implementierung auch für sehr große Graphen richtig ist?
Mittels Induktionsbeweis, die wir in den Tests realisieren.
