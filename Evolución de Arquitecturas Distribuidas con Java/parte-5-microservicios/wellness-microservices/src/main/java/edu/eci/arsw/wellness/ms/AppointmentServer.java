package edu.eci.arsw.wellness.ms;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.LinkedHashMap;
import java.util.Map;

public class AppointmentServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(51051)
                .addService(new AppointmentServiceImpl())
                .build();

        server.start();
        System.out.println("AppointmentService iniciado en puerto 51051");
        server.awaitTermination();
    }

    static class AppointmentServiceImpl extends AppointmentServiceGrpc.AppointmentServiceImplBase {
        private final Map<String, Appointment> appointments = new LinkedHashMap<>();
        private int sequence = 1;

        @Override
        public synchronized void requestAppointment(AppointmentRequest request,
                                                    StreamObserver<AppointmentResponse> responseObserver) {
            String id = "APT-" + sequence++;
            Appointment appointment = Appointment.newBuilder()
                    .setId(id)
                    .setStudentId(request.getStudentId())
                    .setServiceType(request.getServiceType())
                    .setDate(request.getDate())
                    .setStatus(AppointmentStatus.REQUESTED)
                    .build();
            appointments.put(id, appointment);

            responseObserver.onNext(AppointmentResponse.newBuilder()
                    .setAppointment(appointment)
                    .setSuccess(true)
                    .setMessage("Cita solicitada")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public synchronized void cancelAppointment(CancelRequest request,
                                                   StreamObserver<CancelResponse> responseObserver) {
            Appointment appointment = appointments.get(request.getAppointmentId());
            if (appointment == null || !appointment.getStudentId().equals(request.getStudentId())) {
                responseObserver.onNext(CancelResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Cita no encontrada")
                        .build());
                responseObserver.onCompleted();
                return;
            }

            Appointment cancelled = appointment.toBuilder()
                    .setStatus(AppointmentStatus.CANCELLED)
                    .build();
            appointments.put(cancelled.getId(), cancelled);

            responseObserver.onNext(CancelResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Cita cancelada")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public synchronized void getAppointments(StudentRequest request,
                                                 StreamObserver<AppointmentList> responseObserver) {
            AppointmentList.Builder list = AppointmentList.newBuilder();
            for (Appointment appointment : appointments.values()) {
                if (appointment.getStudentId().equals(request.getStudentId())
                        && appointment.getStatus() != AppointmentStatus.CANCELLED) {
                    list.addAppointments(appointment);
                }
            }

            responseObserver.onNext(list.build());
            responseObserver.onCompleted();
        }
    }
}
