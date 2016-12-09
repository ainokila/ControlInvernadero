#include <Ethernet.h>
//Libreria para el sensor de temperatura y humedad
#include <avr/pgmspace.h>
//Libreria para el sensor de temperatura y humedad
#include "DHT.h"
#define DHTPIN 7     

#define DHTTYPE DHT11   // DHT 11

DHT dht(DHTPIN, DHTTYPE);

byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
IPAddress ip(192,168,0, 172);

EthernetServer servidor(80);

bool modoAuto = false;
bool riego = false;
bool cale = false;

void setup() {
  Serial.begin(9600);
   while (!Serial) {
  }

  Ethernet.begin(mac, ip);
  servidor.begin();
  //Agua
  pinMode(3,OUTPUT);
  pinMode(4,OUTPUT);
  pinMode(5,OUTPUT);
  pinMode(6,OUTPUT);

}

void paginaNormal(EthernetClient &cliente){
  
           float humedad =dht.readHumidity();
           float temperatura = dht.readTemperature();

  
          cliente.println("HTTP/1.1 200 OK");
          cliente.println("Content-Type: text/html");
          cliente.println("Connnection: close");
          cliente.println();
          cliente.println("<!DOCTYPE HTML>");
          cliente.println("<html>");
          cliente.println("<meta http-equiv=\"refresh\" content=\"60\">");
          cliente.println("<body>");

            cliente.print("<h1 align=center>Control Climatologico</h1>");
            cliente.print("<h3 align=center>Temperatura ");
            cliente.print("  ");
            cliente.print(temperatura);
            cliente.print(" C</h3>");
 

            cliente.print("<h3 align=center>Humedad ");
            cliente.print("  ");
            cliente.print(humedad);
            cliente.print(" %</h3>");
              
            cliente.print("<h4 align=center>Riego ");
            if(riego){
              cliente.print("<font color='#0dff36'>Encendido</font></h4>");
            }else{
              cliente.print("<font color='#ff0d0d'>Apagado</font></h4>");

            }

            cliente.print("<h4 align=center>Calefaccion ");
            if(cale){
              cliente.print("<font color='#0dff36'>Encendido</font></h4>");
            }else{
              cliente.print("<font color='#ff0d0d'>Apagado</font></h4>");

            }

            String buton = "<button onClick=javascript:location.href=";
            
            cliente.println("<div style='text-align:center;'>");
            cliente.print("Gestion Automatica");
            cliente.println("<br />"); 
            cliente.println(buton+"'./?A=ON\'>");
            cliente.println("ON");
            cliente.println("</button>");
            cliente.println(buton+"'./?A=OFF\'>");
            cliente.println("OFF");
            cliente.println("</button>");
            cliente.println("</body>");
            
            cliente.println("<div style='text-align:left;'>");
            cliente.print("Riego");
            cliente.println("<br />"); 
            cliente.println(buton+"'./?R=ON\'>");
            cliente.println("ON");
            cliente.println("</button>");
            cliente.println(buton+"'./?R=OFF\'>");
            cliente.println("OFF");
            cliente.println("</button>");

            cliente.println("<div style='text-align:right;'>");
            cliente.print("Calefaccion");
            cliente.println("<br />"); 
            cliente.println(buton+"'./?C=ON\'>");
            cliente.println("ON");
            cliente.println("</button>");
            cliente.println(buton+"'./?C=OFF\'>");
            cliente.println("OFF");
            cliente.println("</button>");
            cliente.println("</body>");
            
          cliente.println("</html>");
}


void paginaAutentificacion(EthernetClient &cliente)
{
          cliente.println("HTTP/1.1 401 Authorization Required");
          cliente.println("WWW-Authenticate: Basic realm=\"Area privada\"");
          cliente.println("Content-Type: text/html");
          cliente.println("Connnection: close");
          cliente.println();
          cliente.println("<!DOCTYPE HTML>");
          cliente.println(" </HEAD> <BODY><H1>Acceso no permitido</H1></BODY> </HTML>");
}

void enciendeAgua(){

   digitalWrite(3,HIGH);
   digitalWrite(5,HIGH);
   riego = true;

}

void apagaAgua(){
  digitalWrite(3,LOW);
  digitalWrite(5,LOW);
  riego = false;
}

void enciendeCalefaccion(){
  digitalWrite(4,HIGH);
  digitalWrite(6,HIGH);
  cale = true;
}

void apagaCalefaccion(){
  digitalWrite(4,LOW);
  digitalWrite(6,LOW);
  cale = false;
}

void enciendeAutomatico(){
  modoAuto = true;
}

void apagaAutomatico(){
  modoAuto = false;
}
void gestionAutomatica(){

  int ran = random(4);
  if(ran==0){
    apagaAgua();
    apagaCalefaccion();
  }else if(ran == 1){
    enciendeAgua();
    apagaCalefaccion();
    
  }else if(ran == 2){
    apagaAgua();
    enciendeCalefaccion();
    
  }else if(ran == 3){
    enciendeAgua();
    enciendeCalefaccion();
    
  }
}


void loop() {
  //Serial.print("Fuera if\n");
  //if(modoAuto){
    //gestionAutomatica();
    //Serial.print("Dentro if\n");
  //}
  
  EthernetClient cliente = servidor.available();
  char entrada[80];
  int pos=0;
  boolean autorizado=false;
  
  if (cliente) {
    //Nuevo cliente
    memset(entrada,0,sizeof(entrada));

    boolean lineaVacia = true;
    while (cliente.connected()) {
      if (cliente.available()) {
        
        //Leemos el paquete del socket
        char c = cliente.read();
        
        //Serial.print(c);
        entrada[pos]=c;
        if (pos<sizeof(entrada)-1) pos++;
     
        if (c == '\n' && lineaVacia) {
          if (autorizado)
            paginaNormal(cliente);
          else
            paginaAutentificacion(cliente);  
          break;
        }
        if (c == '\n') {
          lineaVacia = true;
          
          //Acceso printf practica:fr | base64

          if (strstr(entrada,"Authorization: Basic")>0 && strstr(entrada,"cHJhY3RpY2E6ZnI=")>0)
            autorizado=true;

          
          //Manejo de las ordenes 
          
          if (strstr(entrada,"R=ON")>0 ){
              enciendeAgua();
          }
          if (strstr(entrada,"R=OFF")>0 ){
              apagaAgua();
          }

          if (strstr(entrada,"C=ON")>0 ){

            enciendeCalefaccion();
          }
          if (strstr(entrada,"C=OFF")>0 ){

              apagaCalefaccion();
          }

          if (strstr(entrada,"A=ON")>0 ){

              enciendeAutomatico();
          }

          if (strstr(entrada,"A=OFF")>0 ){

              enciendeAutomatico();
          }
          
          
          memset(entrada,0,sizeof(entrada));
          pos=0;
        }
        else if (c != '\r') {
          lineaVacia = false;
        }
      }
    }
    //Esperamos un poco
    delay(1);
    cliente.stop();
    //Desconectamos clientee
  }
}
