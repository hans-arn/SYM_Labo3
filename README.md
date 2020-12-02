## Balises IBeacon

### Les IBeacons sont très souvent présentés comme une alternative à NFC. Vous commenterez cette affirmation en vous basant sur 2-3 exemples de cas d'utilisation concrets.

Les IBeacons et NFC sont des systèmes de localisation intérieur qui peuvent être utiliser dans de nombreux domaines que ce soit dans le e-payements, l'authentification à double facteur... Les IBeacons ont été présenté comme une alternative à la technologie NFC, en effet ils ont des avantages indéniables:

- Une distance de captation bien plus grande allant jusqu'à 100 mètres alors que le système NFC va jusqu'à 10 centimètres.
- Les informations sont dynamiques et peuvent être changer selon différents paramètres. NFC a son fonctionnement directement au sein de sa puce électronique.

Cependant, l'utilisation de NFC ou de IBeacon dépend surtout du context et de l'action.  En effet, nous allons voir 3 cas d'utilisations et expliquer pourquoi malgré ce qui est avancé IBeacon n'est pas une alternative mais est plus complémentaire.

- Dans le cas de e-payement, les applications utilisant les IBeacons sont souvent peu sécurisé et NFC est considéré comme une meilleur technique. NFC demande à l'utilisateur d'être très proche du système, il propose un système de chiffrement des données et même un système de sessions sécurisées. Avec les IBeacon, une personne mal attentionné est plus susceptible de pouvoir récupérer l'échange de message de payement, il peut aussi hacker un IBeacon pour modifier son UUID, majeur et mineur et détourner le système.

- Dans le cas d'un arrêt de bus, l'utilisation de IBeacon est beaucoup plus adapté. Pas besoin de scanner une borne NFC qui ne pourrait fournir qu'une information intangible. Les IBeacons pourraient agir sur la région autour de l'arrêt (un périmètre de 15 mètres autour par exemple) mais en plus s'adapter aux problèmes de trafic car ses données sont modifiables.

- Dans le cas d'un jeu en public, par exemple un bingo ou un quiz, l'utilisation d'un IBeacon est plus intéressante pour permettre à toutes les personnes dans une salle d'accéder au jeu sur leurs téléphones afin d'en faciliter l'usage. Alors qu'avec la technologie NFC à cause de sa faible portée, il faudrait passer avant un par un à une borne.

On remarque alors que IBeacon n'est pas une alternative mais plutôt une solution complémentaire à NFC. L'une étant plus adapté que l'autre et inversement selon les cas d'utilisation.

## NFC

> Dans la manipulation ci-dessus, les tags NFC utilisés contiennent 4 valeurs textuelles codées en UTF-8dans un format de message NDEF. Une personne malveillante ayant accès au porte-clés peut aisément copier les valeurs stockées dans celui-ci et les répliquer sur une autre puce NFC. A partir de l’API Android concernant les tags NFC3, pouvez-vous imaginer une autre approche pour  rendre  plus  compliqué  le  clonage  des  tags  NFC? Existe-il  des  limitations? Voyez-vous d’autres possibilités?

- On pourrait imaginer une sorte d'authentification numérique des données sur le TAG mais cela ne serait malheureusement pas une bonne idée car un attaquant avec du matériel adéquat peut quand même copier **toutes** les données sur le TAG pour les cloner. 

-  Une autre alternatives aux TAG NFC serait d'utiliser une smartcard qui possède un chiffrement asymétrique sur les données inscrites sur la carte. Et ainsi lorsqu'on lirait le support il faudrait entré un mot de passe pour avoir accès aux données. Mais cela n'est valable que si on travaille avec des écrans et qu'on a la possibilité de pouvoir entré la clé asymétrique. 

  Une autre méthode est d'utilisé des supports qui utilisent une communication chiffrée avec le périphérique. Mais le problème, pour que cela soit transparent pour l'utilisateur, est qu'il faudrait stocker la clé de déchiffrement sur le téléphone. Si l'attaquant a compromis ce dernier, nous retomberons sur le même problème.



Mais dans ce projet, le fait qu'un attaquant possède un clone de notre tag ne compromet pas totalement notre infrastructure car nous avons une authentification   

> Est-ce qu’une solution basée sur la vérification de la présence d’un iBeacon sur l’utilisateur, par exemple sous la forme d’un porte-clés serait préférable? Veuillez en discuter

- Cela pourrait être une bonne idée mais le fait est  que si on considère une authentification avec un iBeacon se trouvant dans un périmètre de 15 mètres. Un attaquant pourrait faire un spoofing avec le même ID du iBeacon ce qui reviendrait à le cloner. Cette absence de contact permet aussi de faire des transactions sans que l'utilisateur le sache. 

  Une alternative serait la variante Eddystone de google avec des identifiants éphémère. Mais cela ne serait valable que sur android... 



## Codes-barres

> Quelle est la quantité maximale de données pouvant être stockée sur un QR-code ? Veuillez expérimenter, avec le générateur conseillé de codes-barres (QR), de générer différentes tailles de QR-codes. Pensez-vous qu’il est envisageable d’utiliser confortablement des QRcodes complexes (par exemple du contenant >500 caractères de texte ou une vCard très complète) ?

En faisant des recherches, ce [site](http://qrcode.meetheed.com/question3.php) nous indique qu'un QR-code peut contenir jusqu'à 3kB. 

Avec plus de 500 caractères, le QR-code devient de plus en plus complexe à lire pour une appareil photo. J'ai effectué une tentative avec 1000 caractères et le scan fonctionnait encore, mais arrivé à 2000, ça ne fonctionnait plus. Ayant un appareil photo plutôt récent, ce qui n'est certainement pas le cas de tout le monde, il n'est pas envisageable d'utiliser un QR-code avec autant de caractères.



> Il existe de très nombreux services sur Internet permettant de générer des QR-codes dynamiques. Veuillez expliquer ce que sont les QR-codes dynamiques. Quels sont les avantages et respectivement les inconvénients à utiliser ceux-ci en comparaison avec des QR-codes statiques. Vous adapterez votre réponse à une utilisation depuis une plateforme mobile.

Les QR-codes dynamiques sont des QR-codes dont le contenu peut être modifié sans avoir à remodifier le dessin déjà créé comme pour les QR-codes statiques. Par exemple, on peut y inscrire un lien pour rediriger un utilisateur scannant le motif, mais même si l'on décidait de modifier ce lien, le motif resterait le même en redirigerait l'utilisateur vers le nouveau lien.



**Avantages:**

- Souvent plus petits et donc plus facile à scanner par un appareil mobile
- Correction d'erreurs ou modification du contenu possible
- Possibilité de traquer qui se connecte, depuis où et en extraire des informations pour des statistiques



**Désavantages:**

- Il faut une connexion internet sur le mobile afin de pouvoir rediriger une personne et ce n'est pas forcément le cas de tout le monde