# povmt-mobile
Em uma frase: um assistente que permite à pessoa ocupada acompanhar e avaliar como tem usado seu tempo e aprender com isso.

## Problema atacado

Em uma frase: pessoas ocupadas muitas vezes não sabem como gastam seu tempo, e por isso o usam mal.

Descrição: Com frequência, temos muitas atividades que demandam nosso tempo durante uma semana. Por exemplo, várias disciplinas, mais monitoria, mais um hobby pessoal. Algumas são mais importantes, outras mais divertidas, outras mais fáceis. Caso uma pessoa não tome cuidado com como usa seu tempo, ao fim de uma semana pode perceber que não investiu tempo em uma atividade importante porque investiu tempo demais em outra, que não tinha prazo e não era tão importante. Isso é frustrante, e é esse tipo de situação que queremos evitar com este projeto.

## Solução
Em uma frase: um assistente que permite à pessoa ocupada acompanhar e avaliar como tem usado seu tempo e aprender com isso.

## Referências

- Todo mundo tem coisas demais para fazer
- Use seu tempo onde ele é mais importante
- Como foi sua semana?  (foco na semana)
- Evitaremos falar em gastar tempo. Falaremos em usar, investir, colocar tempo.
- https://new.toggl.com/
- http://www.fastcompany.com/3024249/10-time-tracking-apps-that-will-make-you-more-productive-in-2014

## Glossário

- Atividade – Uma atividade rotineira do usuário na qual ele deseja acompanhar quanto tempo coloca. (ex: LES, Ciclismo, Aprender alemão, ...)
- Ti – Tempo investido. Um registro de tempo colocado em uma atividade.
- Povmt - Pra onde vai meu tempo

## Requisitos não-funcionais

- Facilidade de uso.

# Estórias de usuário

1. Setup do projeto. Esta não é uma US, mas a colocaremos aqui para ressaltá-la no planejamento.

2. Como usuário, registro um novo Ti informando que acabei de usar x horas em uma atividade y, para que depois seja possível computar as horas que gastei nesta atividade. No registro, há uma lista de atividades recentes usadas, e é possível colocar um Ti em uma nova atividade. A interface deve ser o mais fácil de usar possível.

3. Como usuário, visito a página de acompanhamento da minha semana atual, para que eu possa comparar como o tempo que já investi essa semana nas minhas atividades está distribuído entre as atividades. O relatório deve conter o total de horas investidas e um ranking das atividades junto com o total e a proporção das horas investidas esta semana que foram investidas nesta atividade. A interface deve contribuir para o relatório ser interessante e motivar o usuário.

4. Como usuário, vou à minha tela de histórico, e comparo a semana atual com as duas anteriores, para que eu saiba se estou usando meu tempo da mesma maneira, e se meu uso está melhorando ou não.

5. Como usuário, acesso o sistema e todas as suas funcionalidades a partir de vários dispositivos móveis, para que haja uma maior chance de eu lembrar de registrar meus Tis.

6. Como usuário, uso uma identidade google (gmail, ccc, ...) para me autenticar no sistema e garantir que apenas eu acesse meus dados, e que não seja preciso criar um cadastro no sistema.

7. omo usuário, cadastro uma prioridade para cada uma de minhas atividades, e essas prioridades são mostradas em todos os relatórios de uso do meu tempo, para que assim eu saiba se estou investindo mais tempo nas atividades mais importantes.

8. Como usuário, sou lembrado pelo sistema se eu não cadastrei nenhum Ti no dia anterior, para que assim eu esqueça menos de cadastrá-los. Nesta ocasião, quando cadastro um ou mais Tis, ele é registrado no dia anterior. Ao ser lembrado, posso desativar lembretes ou mudar seu horário.

9. Como usuário, ao criar um Ti posso adicionar uma foto para ilustrar a atividade, para que essa foto seja mostrada junto à atividade na interface de agora por diante, e eu ache a interface mais fácil e divertida. A foto poderá ser tirada ou pega da galeria. Uma miniatura da foto será mostrada junto ao nome da atividade sempre que ela aparecer na interface.

10. Como usuário, marco cada atividade que tenho na minha lista de atividades que o sistema conhece como sendo trabalho ou lazer. Caso tenha categorizado as atividades, o sistema me mostra nos relatórios de semana atual e histórico quanto do meu tempo estou usando em cada uma dessas categorias, para que assim eu possa decidir se estou balanceando essas duas partes de minha vida adequadamente.

11. Como usuário, na minha página da semana atual, escolho um subconjunto das atividades que o sistema conhece e ele me mostra um relatório usando apenas Tis dessas atividades, para que eu possa comparar o tempo usado em atividades específicas (por exemplo, nas disciplinas que curso). A interface deixa claro quando estou vendo um relatório com todas as atividades ou apenas algumas. Há uma forma fácil de voltar para todas.

12. Como usuário, na minha página de histórico, escolho um subconjunto das atividades que o sistema conhece e ele me mostra um relatório usando apenas Tis dessas atividades, para que eu possa comparar o tempo usado em atividades específicas (por exemplo, nas disciplinas que curso) ao longo do tempo.

13. Como usuário, cadastro tags nas minhas atividades, e uso essas tags para selecionar subconjuntos das atividades para as quais quero ver relatórios da semana atual ou histórico, para que assim seja mais fácil selecionar subconjuntos de tarefas para esses relatórios.
