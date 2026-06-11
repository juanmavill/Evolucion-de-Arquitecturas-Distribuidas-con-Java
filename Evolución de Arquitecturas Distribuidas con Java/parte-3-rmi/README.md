# Parte III - RPC con Java RMI

## Diagrama

```text
Cliente Java
   |
   | lookup("labInventoryService")
   v
RMI Registry
   |
   | referencia remota
   v
LabInventoryService remoto
   |
   | reservarEquipo("OSC-01")
   v
Resultado al cliente
```

## Ejemplo guiado: MovieService con RMI

Ubicacion: `movie-rmi`.

Compilar:

```bash
javac *.java
```

Ejecutar servidor:

```bash
java MovieRmiServer
```

Ejecutar cliente en otra terminal:

```bash
java MovieRmiClient
```

Evidencia esperada:

```text
Pelicula recibida: 1 - Interstellar (2014) - Christopher Nolan
```

## Ejercicio aplicado 3: Inventario de Laboratorios

Ubicacion: `lab-rmi`.

Datos minimos implementados:

```text
codigo
nombre
laboratorio
estado: disponible o reservado
```

Metodos remotos implementados:

```java
List<String> consultarEquipos()
String consultarEquipo(String codigo)
boolean reservarEquipo(String codigo)
boolean liberarEquipo(String codigo)
```

Compilar:

```bash
javac *.java
```

Ejecutar servidor:

```bash
java LabRmiServer
```

Ejecutar cliente:

```bash
java LabRmiClient
```

Evidencia esperada:

```text
Equipos:
- OSC-01 - Osciloscopio - Laboratorio Electronica - disponible
- ARD-01 - Kit Arduino - Laboratorio Software - disponible
- RPI-01 - Raspberry Pi - Laboratorio Redes - disponible
Consulta OSC-01: OSC-01 - Osciloscopio - Laboratorio Electronica - disponible
Reservar OSC-01: true
Consulta OSC-01: OSC-01 - Osciloscopio - Laboratorio Electronica - reservado
Liberar OSC-01: true
```

## Preguntas de reflexion

**Que cambio al pasar de HTTP a RMI?**

La comunicacion dejo de expresarse como rutas y parametros HTTP. El cliente invoca metodos de una
interfaz remota Java, por ejemplo `reservarEquipo("OSC-01")`, y RMI se encarga de transportar la
llamada entre maquinas virtuales.

**Donde esta definido el contrato de comunicacion?**

El contrato esta definido en la interfaz remota `LabInventoryService`, que extiende `Remote` y declara
los metodos, parametros, tipos de retorno y `RemoteException`.

**Que problemas tendria este sistema si un cliente no esta escrito en Java?**

RMI esta fuertemente acoplado al ecosistema Java. Un cliente en otro lenguaje no podria consumir el
servicio directamente de forma natural; necesitaria un adaptador, otro protocolo o una capa adicional.
