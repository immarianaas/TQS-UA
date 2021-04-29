#Lab 6

####1 e)
O projeto passou a _quality gate_ definida. (Com 1 bug, 0 vulnerabilidades, 1 _security hotspot_ e 28 code smells.)

####1 f)
**bugs:** 
na função `generateRandomDip` na classe `Dip`: `"Save and re-use this "Random"`
É ineficiente criar um objeto `Random` sempre que é necessário um número random, e pode mesmo levar a serem obtidos números não aleatórios.
Para corrigir devia-se instanciar este objeto fora da função, por exemplo, na classe, de forma a que, de cada vez que este método seja invocado, não seja criado um gerador novo.

**vulnerabilities:** 
nenhuma, mas tem um **security hotspot** no mesmo sítio descrito acima, por causa de isso poder levar a que haja repetição de números aleatórios, o que pode levar a problemas de segurança dependendo da sua utilização.

**code smells (major):**
1. na função `generateRandomDip`, o valor `i` inicializado e usado no ciclo `for` está a ser alterado no "corpo" deste ciclo e não "dentro" da sua definição. Este code smell foi identificado 2 vezes nesta função (nos 2 loops existentes).
2. na classe `DemoMain` é utilizada a função `System.out.println` para apresentar mensagens; esta devia ser trocada para se utilizar um **logger**, de forma a ser fácil para o utilizador recuperar/ter acesso aos logs e permitir que esta esteja num formato uniformizado, entre outros. Este code smell foi identificado um total de 7 vezes nesta classe.
3. (critical code smell): a instanciação do gerador de valores aleatórios já mencionado acima

####2 a)
A _technical debt_ é de 2h 31min e corresponde a uma estimativa do tempo que leva a "corrigir"/retirar os code smells.

####2 d)
A _Overall Coverage_ é 71.4%, com 136 _Lines to Cover_, 42 _Uncovered Lines_, _Line Coverage_ de 69.1%, 56 _Conditions to Cover_, 13 _Uncovered Conditions_ e _Condition Coverage_ de 76.8%.

####3
Foi reconhecido no projeto de IES 50 bugs, 16 vulnerabilidades, 2 security hotspots, 472 code smells (que corresponde a 10 dias de debt).
####3 a)
Algumas condições para a *Quality Gate* adequadas para o novo código do projeto de IES podem ser, por exemplo:
* ter uma percentagem de código duplicado <= a 5.0%
* ter <= do que 2 bugs
* não ter nenhum _critical issue_
* ter <=5 _major issue_

