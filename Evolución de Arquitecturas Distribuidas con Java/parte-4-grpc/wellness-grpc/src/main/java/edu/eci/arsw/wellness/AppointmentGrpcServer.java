package edu.eci.arsw.wellness;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.LinkedHashMap;
import java.util.Map;

public class AppointmentGrpcServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(50052)
                .addService(new AppointmentServiceImpl())
                .build();

        server.start();
        System.out.println("Appointment gRPC Server iniciado en puerto 50052");
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
                    .setStudentId(request.getStudent().getId())
                    .setServiceType(request.getServiceType())
                    .setDate(request.getDate())
                    .setStatus(Status.REQUESTED)
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
                    .setStatus(Status.CANCELLED)
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
                boolean sameStudent = appointment.getStudentId().equals(request.getStudentId());
                boolean active = appointment.getStatus() != Status.CANCELLED;
                if (sameStudent && active) {
                    list.addAppointments(appointment);
                }
            }

            responseObserver.onNext(list.build());
            responseObserver.onCompleted();
        }
    }
}
