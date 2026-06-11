# Ejercicio integrador final - Plataforma ECICIENCIA

## Diagrama arquitectonico

```text
Clientes web, moviles o consola
        |
        v
ECICIENCIA Gateway
        |
        +---- AttendeeService
        +---- AgendaService
        +---- ReservationService
        +---- CapacityService
```

## Microservicios propuestos

| Servicio | Responsabilidad |
| --- | --- |
| AttendeeService | Registrar asistentes y consultar sus datos basicos. |
| AgendaService | Publicar actividades, talleres, charlas y consultas por franja horaria. |
| ReservationService | Reservar cupos en talleres y cancelar reservas. |
| CapacityService | Controlar aforo, cupos reservados y asistencia por actividad. |

## Contratos gRPC principales

Archivo propuesto: `eciciencia.proto`.

Servicios incluidos:

```text
AttendeeService
AgendaService
ReservationService
CapacityService
```

Operaciones principales:

```text
RegisterAttendee
GetAttendee
ListActivities
GetActivitiesByTimeSlot
ReserveWorkshopSeat
CancelWorkshopSeat
GetCapacityStatus
RegisterActivityAttendance
```

## API Gateway propuesto

El `ECICIENCIA Gateway` seria el punto de entrada unico para clientes externos. Sus operaciones publicas
podrian ser:

```text
registerAttendee(attendee)
getAgenda()
getActivitiesByTimeSlot(timeSlot)
reserveWorkshopSeat(attendeeId, activityId)
getActivityCapacity(activityId)
registerActivityAttendance(attendeeId, activityId)
```

Internamente, el Gateway coordina llamadas a `AttendeeService`, `AgendaService`,
`ReservationService` y `CapacityService`. Asi evita que el cliente conozca los puertos, contratos y
ubicaciones de cada microservicio.

## Justificacion contra un monolito

No usaria un unico servicio monolitico porque ECICIENCIA mezcla responsabilidades con ritmos de cambio
distintos. El registro de asistentes, la agenda, las reservas y el aforo pueden evolucionar por separado.
Si todo queda en un solo servicio, cualquier cambio en reservas podria afectar consultas de agenda o
registro de asistentes. Una separacion en microservicios permite contratos mas claros, equipos o modulos
mas independientes y una ruta natural para escalar los puntos mas demandados, como consulta de agenda
o reserva de talleres.

## Reflexion final

El taller muestra una evolucion progresiva. Con sockets TCP se entiende la comunicacion remota en su
nivel mas directo, pero tambien aparece el costo de definir protocolos manuales. HTTP mejora la
interoperabilidad porque organiza solicitudes en rutas, metodos y parametros conocidos por muchos
clientes. RMI simplifica la programacion para Java al convertir la comunicacion en invocacion de
metodos, aunque queda limitado a ese ecosistema. gRPC introduce contratos formales con `.proto`, tipos
fuertes e interoperabilidad entre lenguajes, lo que facilita construir servicios distribuidos modernos.
Cuando el dominio crece, los microservicios ayudan a separar responsabilidades y datos, pero aumentan
la complejidad operativa. Finalmente, el API Gateway reduce el acoplamiento del cliente frente a muchos
servicios, aunque debe cuidarse para que no acumule demasiada logica de negocio. En ECICIENCIA, esta
evolucion justifica pasar de una solucion simple a una arquitectura distribuida con contratos claros,
servicios responsables y un punto de entrada centralizado.
