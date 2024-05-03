En esta aplicación se pueden visualizar los países que están en el api de restcountries, me basé en la documentación del api y por medio de este endpoint se realizó el consumo:
https://restcountries.com/v3.1/all

•	Componentes de la aplicación (requerimientos técnicos):
•	La aplicación tiene un patrón de diseño MVVM.
•	Inyección de dependencias con Dagger Hilt.
•	Consumo del servicio con retrofit.
•	Base de datos local con ROOM tanto para el Login o creación de usuario, como para guardar los países de la respuesta del consumo, esto con el fin de usar la aplicación de manera offline y que no haya complicaciones, cabe recalcar que en primera instancia si la aplicación se ejecuta por primera vez y no contiene datos en la base de datos no podrá mostrar los países, ya que se necesita de una primera instancia con internet para guardar los países a la base de datos.
•	Vistas nativas con xml en su mayoría con "ConstraintLayout", "FrameLayout" y "NestedScrollView" para el tema de los recycler y fragments con mucho contenido.
•	Navegación entre fragments con Safeargs "Navigation Components" tengo un NavGraph para la navegación de los fragments tanto para el login como del home y demás vistas.
•	Procesamiento de imágenes con Picasso.
•	Uso de corrutinas para el consumo del servicio, inserción a la bd y funcionalidades en segundo plano, tanto para el consumo asíncrono del servicio.
•	El proyecto esta estructurado por capas, cada una con una funcionalidad única, igual que los fragments con responsabilidad única siguiendo los principios Solid

Especificaciones y flujo de la aplicación:


•	Primera vista (Introducción):

![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/5f031ef2-b0e3-4c58-a51c-bb78ea029875)

En esta primera vista es la introducción a la aplicación que especifica un poco que es lo que hace la APP


•	Segunda vista (Opciones):

![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/a07db3cb-711b-4568-be1c-9e1292874eec)

Al darle en el botón de "Comencemos" de la vista principal, nos llevará a esta siguiente vista que muestra 2 opciones, una llevara a otra vista para que el usuario se registre y la otra opción si el usuario ya tiene una cuenta se podrá logear con el email y contraseña


•	Tercera vista (Registro):

![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/f08180b1-64a4-4233-92ca-46428868d76c)

Al seleccionar el botón de "Registrar" de la vista anterior, llevará al usuario a esta siguiente vista, dónde se le solicitaran unos datos como: "Email", "Nombre" y "Contraseña", esta vista tiene unas validaciones para que los campos no estén vacíos, que el email este bien escrito y que la contraseña tenga mínimo 8 caracteres, de igual manera si el usuario cuenta con el correo, al seleccionar la parte que indica "¿Ya tienes una cuenta? ingresar" lo llevara a la vista del login para que el usuario se pueda logear.


•	Cuarta vista (Login):

![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/606afa28-68c0-4766-ada2-bde9454745d7)

Al seleccionar el botón de "Login" o en su defeco al seleccionar "¿Ya tienes una cuenta? ingresar" de la vista de registro, no llevará a la vista de Login(Ingreso), en esta vista solicitará al usuario que digite su "correo" y "contraseña" previamente creados, si el usuario no cuenta con una cuenta, desde esta vista puede navegar a la vista de Registro y poder autenticarse en la aplicación, al usuario tener correctamente diligenciado el correo y contraseña puede navegar a la siguiente vista.


•	Quinta vista (Home):

![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/468d8acc-e14f-4fa6-9fa9-67ab0a869268)

El usuario al ingresar correctamente sus credenciales la aplicación le permitirá navegar a esta vista que contiene una opción de search (Buscar) con imagen de lupa, también puede organizar los items del recycler con el botón flotando al otro extremo de la lupa, y abajo de estas opciones el usuario podrá ver todos los países suministrados por el servicio, el usuario puede navegar hacia abajo para poder visualizar todos los países hasta llegar a su límite, en esta vista de home en la parte inferior aparecen 2 imágenes, una Home(imagen de casa) y la otra Perfil (imagen de una persona), funcionalidades:
    
    Al seleccionar la lupa se desplegará una caja de texto donde el usuario podrá digitar el país o capital de un país y automáticamente la aplicación empezará a aplicar un filtro que mostrará los países según escriba.
  
  	 Al seleccionar el botón flotante, este organizara los países de forma alfabética (A-Z).
  
  	 Al seleccionar un país lo llevara a ora vista con el detalle del país seleccionado.
  
  	 Al seleccionar la imagen de la persona, el usuario navegará a otra vista del perfil de usuario.
  

•	Sexta vista (Deatil):

![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/73f43cc1-63ab-45b3-8014-457268d7a432)


![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/97c0b9a4-90ac-49b3-bf78-0861724bdd15)

El usuario al seleccionar un país de la primera vista navegará a esta siguiente vista que muestra a más detalle sobre el país, que mostrará la siguiente información:

  	La bandera del país.
  
  	El nombre del país.
  
  	La capital del país.
  
  	El código de 3 letras del país.
  
  	La población que tiene el país.
  
  	El estado del país.
  
  	La región.
  
  	La sub Región.
  
  	El día de la semana dónde inicia la semana el país.
  
  	El contiene que pertenece.
  
  	Y por último los países que limitan con el país en gestión y una breve información como es: La bandera, El nombre del país y la capital.
  
  	En esta vista el usuario puede navegar a la vista anterior con el botón "exit" en la parte superior izquierda.


•	Séptima vista (Perfil):

![image](https://github.com/MagnerRivera/CountriesApp/assets/103458372/9040629b-7e8a-46a3-b15c-537e2b68ea2a)

El usuario al navegar a esta vista podrá ver una única opción que es la de salir "Log Out", al seleccionar esta opción se mostrará una alerta al usuario con 2 opciones "Cancelar" y "Sí" si el usuario cancela se queda con el inicio de sesión que estaba, pero si le da en "Sí" se cierra la sesión y el usuario tendrá que volver a logearse.
