# Control de Invernadero

## Material Usado:

* Placa Arduino Uno R3
* Ethernet Arduino
* Sensor DHT11 para temperatura y humedad.
* Relays hasta 10A.

## Objetivo

Poder controlar la humedad y la temperatura desde una interfaz y además podamos encender diversos intrumentos como los calentadores y el agua.

## Diagrama de estados del servidor para 2 relays.

![DiagramaEstados](https://github.com/ainokila/ControlInvernadero/blob/master/example/Diagrama%20de%20estados.png)

## Interfaz de Usuario

### Interfaz Web

Cuenta con una interfaz en la que se pueden controlar los sensores y los relays para encender lo que se necesite, para el ejemplo estan usandose solamente 2.

![Login](https://github.com/ainokila/ControlInvernadero/blob/master/example/FiguraLogin.PNG)

![Web](https://github.com/ainokila/ControlInvernadero/blob/master/example/Figura2.PNG)

## Linea de Comandos Java

Tambien se ha implementado una aplicacion en linea de comandos usando Sockets .

![Terminal](https://github.com/ainokila/ControlInvernadero/blob/master/example/Terminal.png)



