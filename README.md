# HiveGame
Recriação do jogo Hive em Java

ERROS
Atualmente as peças Ant e Spider movem-se da mesma maneira - sem limite de casas
Spider dev-se mover exatamente 3 casas

//TODO 
completar classe Spider





RELATÓRIO - HIVE Board Game - 11 de Julho de 2020 - INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA 

INTRODUÇÃO 
Neste trabalho é pedida a implementação em Java Swing do jogo de tabuleiro Hive. O 
jogo consiste de 2 jogadores com 11 peças cada. Peças são colocadas a partir da 
primeira sem nunca quebrar a construção. Podem ser movimentadas de acordo com regras 
especificas para cada tipo de peça. Termina quando um dos jogadores conseguir rodear 
a QueenBee do outro. 
O jogo deve permitir que (com um clique do rato) se escolham novas peças para 
colocar ou selecionem peças já colocadas para mover. 
É fornecido um projeto Java (inacabado) e o respetivo UML.

GUI 
Começou-se por desenvolver a GUI. 
O tabuleiro já está construído na classe Board. 
Na mainLabel do método init() na classe Game criou-se a banner no topo da janela 
onde aparece o nome do jogo e no nome do jogador atual. 
No construtor da classe PlayerData criaram-se e inicializaram-se todos os botões e 
Labels de cada jogador – Nome do jogador; Cor do Jogador; Botão para cada peça; e 
um display com o número de jogadas. 
No método init() da classe Game foram criados os botões de controlo: New Game, 
movimentação, Change Player e Give Up. 
Por baixo é colocada uma log box que vai atualizando a mensagem de acordo com os 
acontecimentos do jogo. É usada a Label lb_message. 
No método buildMenu() construiu-se uma barra de opções com os itens New Game, Main 
Menu, About, Scores, Rules, Music(on/off) e Quit. 
Foi ainda adicionado um menu inicial. No método mainMenu() da classe Game incluiuse uma Label com o nome do jogo, um conjunto de botões – Play, Rules, High Scores, 
About, Quit.

COLOCAÇÃO MANUAL DAS PEÇAS 
De seguida testou-se a colocação das peças. 
Escolhe coordenadas do tabuleiro - O método Board.addPiece(Piece p, int x, int y) 
chama o método BoardPlace.addPiece(Piece p) para colocar a peça p recebida, nas 
coordenadas x y do array Board. 
Associa ao BoardPlace - O addPiece do BoardPlace adiciona a peça ao ArrayList de 
peças do BoardPlace em questão – ArrayPieces(piece); 
Pinta peço no tabuleiro - O método paintComponent(Graphics g) ‘pinta’ o BoardPlace 
com as cores e primeira letra da peça a colocar. 
String str = getPiece().toString().substring(0, 1); 
g.setColor(Game.getColorFromPlayer(getPiece().isFromPlayerA())); 
g.fillPolygon(polygon); 
g.setColor(getPiece().getColor()); 
g.drawString(str, this.baseX + 8, this.baseY + 18);

COLOCAÇÃO POR SELEÇÃO E MUDANÇA 
DE JOGADOR 
Passou-se á COLOCAÇÃO POR SELEÇÃO. 
Na classe Game, no método clickOnPieceLabelOnSidePanel(JButton button, Piece piece)
recebem-se como argumentos um botão e uma peça. 
Associar botão ao tipo de peça - É atribuído um Action Listener ao botão. No clique 
são atribuídos os valores recebidos ao currentPiece e ao lastButton e cria uma 
HiveLabel. 
Colocar peça no tabuleiro com um clique - Na classe Board foi acrescentado um Mouse 
Listener para receber o clique para colocação da peça. 
if (game.move == false) { 
while (count < 1) { 
clickOnBoard(e.getX(), e.getY(), game.currentHiveLabel); 
 game.lastButton.setEnabled(false); 
 game.lastButton.setBackground(Color.gray); 
count++; 
} 
} 
Se a variável move for falsa, indica que a peça atual é nova – está a ser colocada, 
não movida. O count garante que só é colocada uma peça, independentemente do número 
de cliques. 
É chamado o clickOnBoard com o x e y do ecrã (pixels) – valida o boardPlace onde 
ocorreu o clique e chama o clickOnBoard da classe game onde é feita a colocação da 
peça no BoardPlace com as coordenadas calculadas. 
O botão usado é desabilitado para evitar repetição de peças. 
A MUDANÇA DE JOGADOR, para além de ser um dos passos sugeridos, é uma das regras 
do jogo. 
R6: Um jogador pode passar a sua vez, contando como um seu movimento. 
Deve trocar jogador atual, trocar ‘recursos’ do jogador (PlayerData) e atualizar o 
número de jogadas. 
A implementação da mudança de jogador é feita no método changePlayer na classe Game. 
O changePlayer troca o valor do boolean isPlayerAToPlay (jogador atual) e troca a 
PlayerData atual. Aumenta o número de jogadas, atualiza a Label com o número de 
jogadas, coloca o painel do jogador atual inativo e ativa o do novo jogador atual 
e atualiza a banner do jogo. Finalmente faz reset à peça e HiveLabel atual e prepara 
as variáveis globais move e firstClick para permitirem movimentação de peça.

REGRAS DE MOVIMENTAÇÃO E 
COLOCAÇÃO 
A validação da colocação e movimentação de peças de acordo com as regras é feita na 
sua maioria no método clickOnBoard da classe Game. 
Começa-se por verificar se é uma colocação ou seleção (retira peça do tabuleiro). 
if (board.getBoardPlace(x, y).getPiece() == null || currentPiece instanceof 
Beetle) { 
Colocação só pode ser feita se o BoardPlace de destino for vazio ou se a peça a ser 
colocada for um Beetle. 
Caso contrário é retirada a peça para mover – linha 1183. 
Verifica-se ainda se é uma colocação de peça nova por clique no botão ou colocação 
de uma peça existente por movimentação. 
if (move == false) { - linha 1158 
Para cada situação são implementadas as várias regras de colocação/movimentação. 
REGRAS GERAIS 
NÃO COLOCAR AO PÉ DE PEÇAS DO OPONENTE 
R1: Quando se coloca uma peça no tabuleiro ela não pode ter vizinhos inimigos, à 
exceção da primeira peça do segundo jogador a jogar. 
if (onlyHaveFriendlyNeighbors(x, y, currentPiece) == false 
&& playerAData.getNumberOfPiecesOnBoard() >= 1 
&& playerBData.getNumberOfPiecesOnBoard() >= 1) { 
Se já existirem pelo menos uma peça de cada jogador, não é permitida a colocação de 
uma peça ao pé de peças do oponente. 
COLOCAR A RAINHA ATÉ À QUARTA JOGADA 
R2: A abelha rainha (QueenBee) tem de ser colocada obrigatoriamente até à 4ª jogada. 
Só se pode fazer movimentos de peças de um jogador depois de ele ter colocador a 
sua abelha rainha. 
if (currentPlayerData.getNumberOfMoves() >= 3 
&& currentPlayerData.isQueenBeeAlreadyOnBoard() == false) { 
queenBytheFourth(x, y); 
Se na quarta jogada a rainha ainda não tiver sido colocada vai ser forçada a sua 
colocação independentemente da peça selecionada – método queenBythefourth(). 
 currentPiece = new QueenBee(g, isPlayerAToPlay); 
 lastButton = currentPlayerData.Queen; 
 lastButton.setEnabled(false); 
 lastButton.setBackground(Color.gray); 
 placePiece(x, y); 
Tem-se ainda que nenhuma peça pode ser movimentada até à colocação da rainha. Assim 
uma peça só pode ser escolhida para movimentação se a rainha estiver em jogo (linha 
1183 na classe Game). 
if (board.getBoardPlace(x, y).getPiece().isFromPlayerA() == isPlayerAToPlay && 
currentPlayerData.isQueenBeeAlreadyOnBoard()) { 
NÃO QUEBRAR A HIVE 
R4: O Hive deve ser sempre um. Os movimentos das peças também não devem gerar dois 
Hives, mesmo que momentaneamente, ou seja, os lugares de origem e de destino de 
qualquer movimento de uma posição, deve ser conectado por outra(s) peça(s), que não 
aquela que se quer mover. 
[Um movimento de várias posições (exceto no caso do gafanhoto) é uma composição de 
vários movimentos de uma posição. Todas as peças (bichos) têm de respeitar esta 
regra.] – foi feita uma implementação diferente com ponto de partida e destino em 
que o movimento é validado no destino. 
Garantir que a peça colocada não está afastada da Hive. 
if (board.justOneHive(x, y) == false && playerAData.getNumberOfPiecesOnBoard() + 
playerBData.getNumberOfPiecesOnBoard() >= 1) { 
Garantir que a peça selecionada não quebra a Hive. 
if (board.getPiecesFromThisPoint(x, y, new ArrayList<Piece>()) < 
(playerAData.getNumberOfPiecesOnBoard() + playerBData.getNumberOfPiecesOnBoard()) 
- 1) { 
Nestes casos a peça colocada de volta no BoardPlace de onde foi retirada. 
SÓ COLOCAR PEÇAS EM POSIÇÕES ONDE SEJA FISICAMENTE POSSIVEL 
R7: As peças Abelha Rainha (QueenBee), Aranha (Spider) e formiga (Ant) só se podem 
deslocar se o movimento for fisicamente realizável em cada passo. Cada peça para 
poder colocada no tabuleiro tem de poder deslizar fisicamente do rebordo para o 
lugar de destino. 
O método para validação do “slide in” – só permitir mover para locais para onde é 
fisicamente possível deslizar a peça sem afastar as existentes – foi terminado 
(canPhysicallyMoveTo() – linha 1252 da classe Game), mas não foi testado e não 
chegou a ser implementado. 
                                                                                                        
REGRAS ESPECIFICAS DE TIPO DE PEÇA 
A última linha de verificação para uma peça ser colocada é feita no moveTo() – 
método abstract da classe Piece, que extende a todas as classes de tipo de peça. É 
a validação da regra de movimentação especifica do tipo de peça.
                                                                                                        
                                                                                                        
QUEENBEE 
RQ: A abelha rainha (QueenBee) só se pode deslocar uma posição. 
for (Direction d : Direction.values()) { 
 if (game.getBoard().getBoardPlace(game.getBoard().getNeighbourPoint(x, y, 
d).x, game.getBoard().getNeighbourPoint(x, y, d).y) == 
game.getBoard().getBoardPlace(game.getBoard().oldX, game.getBoard().oldY)) { 
 return true; 
 } 
} 
return false; 
Faz-se a verificação de todos os BoardPlaces vazios á volta da rainha. Tendo em 
conta os destinos disponíveis de acordo com as regras gerais só se move uma casa. 
BEETLE 
RB: Os escaravelhos (Beetle) só se deslocam 1 posição mas podem subir para cima de 
outras peças colocadas, mesmo ficando uns por cima dos outros. É a única peça que 
pode subir para cima de outras. A peça que estiver por cima é que pode ser 
movimentada e que conta como a peça daquela posição. O escravelho não pode ser 
colocado inicialmente em cima de outra peça. 
Usa o mesmo mecanismo da rainha contando com a exceção no clickOnBoard que garante 
que a pode ser colocado por cima de outras peças. 
if (board.getBoardPlace(x, y).getPiece() == null || currentPiece instanceof 
Beetle) { 
Conta ainda com o método beetle_vs_piecesOnBoard(), que garante que o número de 
peças se mantém correto, independentemente das peças visíveis (necessário para 
impedir criação de várias Hives). 
GRASSHOPPER 
RG: Os gafanhotos (Grasshopper) têm de saltar, mas sempre em linha reta e por cima 
de pelo menos uma posição ocupada. Mas não podem saltar por cima de posições não 
ocupadas. 
O moveTo() do Grasshopper valida todas as casas vizinha só permitindo mover nas 
direções que começam com uma casa ocupada, visto que não pode saltar casas vazias. 
for (Direction d : Direction.values()) { 
 int rX = (int) game.getBoard().getNeighbourPoint(game.getBoard().oldX, 
game.getBoard().oldY, d).getX(); 
 int rY = (int) game.getBoard().getNeighbourPoint(game.getBoard().oldX, 
game.getBoard().oldY, d).getY(); 
if (game.getBoard().getBoardPlace(rX, rY).getPiece() != null) { 
 if (straightSearch(rX, rY, x, y, d)) { 
 return true; 
 } 
 } 
} 
return false;
De seguida valida as casas a partir da casa vizinha numa linha reta para cada 
direção, até encontrar a casa de destino ou uma casa nula. 
int X = (int) game.getBoard().getNeighbourPoint(fromX, fromY, d).getX(); 
int Y = (int) game.getBoard().getNeighbourPoint(fromX, fromY, d).getY(); 
if (game.getBoard().getBoardPlace(X, Y).getPiece() == null) { 
 if (X == toX && Y == toY) { 
 return true; 
 } else { 
 return false; 
 } 
} else { 
 return true && straightSearch(X, Y, toX, toY, d); 
} 
SPIDER 
RS: As aranhas (Spider) devem deslocar-se sempre em movimentos de 3 posições 
diferentes . 
Para a Spider pensou-se numa implementação semelhante ao Grasshopper. Usando três 
fases de validação para cada casa andada. 
1 - São validadas as casas á volta do ponto de partida. 
2 - Para as que forem nulas. Valida-se todos os neighbours exceto o que se 
encontra na direção inversa para impedir que se volte a verificar a casa de 
partida. 
3 – Novamente, para os nulos valida-se os neighbours em todas as direções, exceto 
a inversa. Se forem casas nulas verifica-se as coordenadas são as de destino. Caso 
positivo a peça é colocada. 
O método não foi completado. Ficou, ainda, em fase de testes. 
No entanto, as peças Spider existem com um comportamento igual ao da Ant, para 
garantir que o jogo conta com o número de peças esperado. 
ANT 
RA: As formigas (Ant) podem deslocar-se um qualquer nº de posições. 
Tendo em conta que a formiga se move apenas de acordo com regras genéricas – 
comuns a todas as outras peças – não foi implementado um método específico para a 
formiga – não existe nenhum moveTo() especifico da formiga. O bom funcionamento é 
garantido por todas as regras previamente implementadas.
                                                                                                        
FINAL DE JOGO 
R5: Regra de fim de jogo: o jogo termina quando uma abelha rainha (QueenBee), ou 
as duas (empate), estiver(em) totalmente rodeada(s) por (quaisquer) peças. 
Deteção de final de jogo é feito com recurso aos métodos checkFinishGame(), 
checkFinishGame(boolean playerA) e doFinishGameActions() na classe Game. 
O segundo método referido verifica se alguma rainha está rodeada. Usa um 
funcionamento parecido com o do justOneHive() - procura a rainha e verifica todos 
os naighbous. Caso nenhum esteja vazio – retorna true, caso contrário false. 
O checkFinishGame() chama o método anterior sobre o jogador A e B caso algum dos 
dois seja verdadeiro, é colocado o valor do playerWon do respetivo jogador a true. 
O doFinishGameActions() é chamado a cada clickOnBoard(). É onde é verificado se 
algum dos jogadores (ou os dois) ganhou. Dependendo do(s) jogador(es) que 
ganhou(/aram) é chamado um ecrã de final de jogo com uma banner diferente. 
                                                                                                      
ACESSIBILIDADE 
São pedidos alguns FUNCIONAMENTOS ESPECÍFICOS na aplicação. 
VERIFICAR PEÇAS “POR BAIXO” DO BEETLE 
Para ver todas as peças “por baixo” do Beetle deve-se clicar sobre o mesmo com o 
botão direito do rato. A lista de peças é mostrada na log box. 
if (e.getButton() == MouseEvent.BUTTON3) { 
 String status = ""; 
 for (int y = 0; y < DIMY; y++) { 
 for (int x = 0; x < DIMX; x++) { 
 if (board[x][y].isInsideBoardPlace(e.getX(), e.getY()) == true 
&& getBoardPlace(x, y) != null) { 
for (int i = 0; i < getBoardPlace(x, y).pieces.size(); 
i++) { 
String plr = getBoardPlace(x, 
y).pieces.get(i).isFromPlayerA() ? " from Player A; " : 
" from Player B; "; 
 int nr = i + 1; 
 status += "#" + nr + ": " + getBoardPlace(x, 
y).pieces.get(i) + plr; 
 } 
game.setStatusInfo(status); 
 } 
 } 
 } 
} 
  
MOVER COLMEIA 
Os botões de mover colmeia são implementados com recurso aos métodos moveHiveX(), 
sendo X a direção. 
Cada método vai verificar todos os boardPlaces e, no caso de estarem ocupados, 
retira a peça e coloca-a uma casa mais na direção escolhida. 
Valida sempre na direção inversa, para verificar primeiro as peças mais na direção 
especifica. 
exemplos: 
Caso NORTE, começa a 0,0 para encontrar primeiro peças mais a norte; 
Caso SUL começa a 0,MAX para validar primeiro peças mais a SUL. 
  
GUARDAR SCORE COM USERNAME 
No ecrã de final de jogo é apresentado um textField a pedir o username do 
vencedor. 
Os scores são guardados num ficheiro de texto na raiz do projeto. 
A gestão dos scores recorre aos seguintes métodos e variáveis da classe Game: 
scoreList - HashMap onde se guardam os users e scores associados. 
maxLength – máximo de scores que se pode guardar. 
name e nr - para guardar os valores do user introduzido e do número de jogadas 
necessárias para ganhar. 
viewScores - é chamado pelo botão View Scores na barra de opções ou no High Scores 
do menu inicial. Vai chamar a frame onde se vê a lista. 
Sort – ordena lista. 
addScore - verifica se o score recebido pode ser guardado dependendo da quantidade 
de scores guardada e da posição em que este se encontra – recorre ao sort para 
ordenar a lista. 
checkName – Valida o name. Caso o name já exista é aceite com um ‘*’. 
checkScore - Para os scores. Como não são guardados scores repetidos, são 
multiplicados por 100. E, quando recebido score já existente é lhe subtraído 1.  
  
UML
A implementação seguida levou a que algumas classes e métodos não fossem 
utilizados. Não sendo necessários ao bom funcionamento do jogo foram comentados e 
foi atualizado o UML do projeto. 
Explicação mais detalhada de cada método pode ser vista nos comentários no código. 
Métodos e classes não utilizados foram comentados e colocado no final dos 
respetivos ficheiros (classes). 
