package edu.eci.arsw.wellness.ms;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;

public class GymServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(51053)
                .addService(new GymServiceImpl())
                .build();

        server.start();
        System.out.println("GymService iniciado en puerto 51053");
        server.awaitTermination();
    }

    static class GymServiceImpl extends GymServiceGrpc.GymServiceImplBase {
        private final List<GymReservation> reservations = new ArrayList<>();
        private int sequence = 1;

        @Override
        public synchronized void reserveGymSession(GymReservationRequest request,
                                                   StreamObserver<GymReservationResponse> responseObserver) {
            GymReservation reservation = GymReservation.newBuilder()
                    .setId("GYM-" + sequence++)
                    .setStudentId(request.getStudentId())
                    .setTimeSlot(request.getTimeSlot())
                    .setStatus("RESERVED")
                    .build();
            reservations.add(reservation);

            responseObserver.onNext(GymReservationResponse.newBuilder()
                    .setReservation(reservation)
                    .setSuccess(true)
                    .setMessage("Sesion de gimnasio reservada")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public synchronized void getGymReservations(StudentRequest request,
                                                    StreamObserver<GymReservationList> responseObserver) {
            GymReservationList.Builder list = GymReservationList.newBuilder();
            for (GymReservation reservation : reservations) {
                if (reservation.getStudentId().equals(request.getStudentId())) {
                    list.addReservations(reservation);
                }
            }

            responseObserver.onNext(list.build());
            responseObserver.onCompleted();
        }
    }
}
