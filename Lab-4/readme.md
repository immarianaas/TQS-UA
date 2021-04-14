# Lab-4

### Ex 1
**a)**
Um exemplo de *methods chaining* expressivo de **AssertJ** é visível na linha 65 do ficheiro `EmployeeRepositoryTest.java`:
```java
assertThat(allEmployees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), ron.getName(), bob.getName());
```
Outro exemplo está no ficheiro `EmployeeRestControllerIT.java`:
```java
assertThat(found).extracting(Employee::getName).containsOnly("bob");
```
Outros exemplos da sua utilização encontram-se em todos os ficheiros *.java* de testes no projeto, exceto 2: `JsonUtil.java` e `EmployeeController_WithMockServiceIT.java`.


**b)**
O ficheiro `EmployeeService_UnitTest.java` possui uma classe de testes que utiliza a estratégia de fazer **mock** do comportamento do repositório.


**c)**
A anotaçáo **@Mock** indica ao Mockito para criar e instanciar um "mock" para permitir que não seja necessário usar (e inicializar, etc) uma implementação real da interface ou classe que está anotada. Esta anotação não é limitada ao Spring Boot, podendo ser utilizada com qualquer outra framework.

Por outro lado, **@MockBean** existe apenas dentro da framework Spring. No ambiente de testes do Spring Boot, pode-se criar um Spring Context dedicado para os testes, permitindo realizar testes de integração de multiplas classes. Este contexto pode ser populado utilizando o Spring Context completo, ou "sliced contexts". Para inicializar este contexto, todas as dependências dos *beans* têm de ser satisfeitas, o que pode ser feito instanciando-as dentro do contexto. Por vezes pode não ser necessário utilizar a verdadeira implementação do beam, e então pode-se criar **mocks** destes para o efeito de inicializar o Spring Text Context.


**d)**
O ficheiro será usado quando a anotação `@TestPropertySource` é usada com um argumento a "apontar" para ele. Trata-se de um ficheiro de configuração para o contexto, que, neste caso, indica qual a base de dados que deve ser usada, e como aceder a ela. Neste contexto, ele irá permitir utilizar uma base de dados real nos testes presentes na classe onde essa anotação está.
