# Hyperledger Fabric - SDK Java

#### Introdução
Está aplicação tem como finalidade criar uma API REST, utilizando Java Spring Boot, para comunicação com a rede Fabric, podendo assim gerenciar todo a rede e suas funcionalidades. A comunicação entre o cliente e a rede fabric utiliza chamadas do SDK Java, disponibilizado em [Hyperledger Fabric SDK Java](https://github.com/hyperledger/fabric-sdk-java)

#### Funcionalidades
As seguintes funcionalidades foram desenvolvidas para esta aplicação.

##### Identidades
* Registro de novas identidades para organizações
* Revogação de identidades [PROGRESSO]
* Listagem de identidades 

##### Canal
* Criar canal [PROGRESSO]
* Conexão com canal para transações
* Listagem de canais, de acordo com o acesso da organização

##### Peer
* Listagem de chaincodes instalados na organização
* Listagem de chaincodes instanciados no canal

##### Chaincodes

###### Identidades Administradoras
* Instalação de chaincode
* Iniciação de chaincode, com política de endorsamento
* Atualização de chaincode, com política de endorsamento

###### Identidades Membros
* Invoke chaincode
* Query chaincode

##### Transações
* Informações sobre transações utilizando ID
* Informações sobre o bloco, utilizando uma ID
* Informações sobre o bloco, utilizando o número do bloco

#### Organização do projeto
-> src/main/java: *Projeto java* <p>
-> chaincode/src/github.com/hyperledger-fabric-go-chaincodes: *Localização dos chaincodes*<p>
-> endorsementypolicy: *Localização dos arquivos de política*<p>
-> network-basic: *Localização dos arquivos de configuração da rede Fabric*<p>
-> wallet: *Localização das identidades pertencentes a rede**<p>

**OBS: O projeto inicial conta com a identidade de administrador da oganização, presente em .dat**

#### Primeiros passos
Para iniciar a aplicação é necessario:

**a.** Importar projeto 

**b.** Rede Fabric em pé

**c.** Arquivo de conexão definindo a rede

**d.** Executar a aplicação Java

**e.** Acessar swagger

#### Importar projeto
1. Abrir IDE, neste caso o Eclipse
2. Arquivos
3. Importar
4. Maven
5. Projetos maven existentes 
 
#### Levantando a rede fabric
1. Navegar para a pasta *./network-basic*
2. Executar o script start *~./start~*
3. Aguardar o termino do script

#### Arquivo de conexão definindo a rede
A rede fabric levantada no passo anterior possui a seguinte configuração:
* 1 organizações
* 1 peers/org
* 1 orderers
* 1 canais

**PS: O arquivo de configuração pode ser visto em ./network-basic/connection**

#### Acessando a página swagger 

Para visualização de rotas e serviços, acessar: http://localhost:3000/swagger-ui.html

##### Executar a aplicação java
Utilizando uma IDE de sua preferencia
* Importar projeto
* Executar o main, como aplicação Java.
* Fazer requisições pelo localhost:3000/...

**OBS: Para facilitar o teste de outras redes fabric, com diferentes arquiteturas, o [script](https://gitlab.com/natanael.soares/network) a seguir é recomendado. Onde é possível desenhar uma arquitetura diversificada e obter todo os arquivos necessarios para configuração da aplicação, sendo necessario a substituição da pasta network pela nova configuração e a criação de novas identidades.**

#### Fluxo de utilização da aplicação

##### Fluxo de identidade
![Identidades](./fluxograma/Identidades.png)

##### Fluxo transações
![Fluxo](./fluxograma/Transacoes.png)

**Utilizar a identidade adminorg1, para conexão.** 
**Utilizar o canal mychannel, para as transações.** 



