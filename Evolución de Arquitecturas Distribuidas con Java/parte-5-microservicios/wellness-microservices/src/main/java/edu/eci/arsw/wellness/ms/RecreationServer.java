package edu.eci.arsw.wellness.ms;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecreationServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(51054)
                .addService(new RecreationServiceImpl())
                .build();

        server.start();
        System.out.println("RecreationService iniciado en puerto 51054");
        server.awaitTermination();
    }

    static class RecreationServiceImpl extends RecreationServiceGrpc.RecreationServiceImplBase {
        private final Set<String> resources = new HashSet<>();
        private final List<RecreationReservation> reservations = new ArrayList<>();
        private int sequence = 1;

        public RecreationServiceImpl() {
            resources.add("BALL-01");
            resources.add("CHESS-01");
            resources.add("TABLE-01");
        }

        @Override
        public synchronized void reserveRecreationResource(RecreationReservationRequest request,
                                                           StreamObserver<RecreationReservationResponse> responseObserver) {
            if (!resources.contains(request.getResourceId())) {
                responseObserver.onNext(RecreationReservationResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Recurso recreativo no existe")
                        .build());
                responseObserver.onCompleted();
                return;
            }

            RecreationReservation reservation = RecreationReservation.newBuilder()
                    .setId("REC-" + sequence++)
                    .setStudentId(request.getStudentId())
                    .setResourceId(request.getResourceId())
                    .setStatus("RESERVED")
                    .build();
            reservations.add(reservation);

            responseObserver.onNext(RecreationReservationResponse.newBuilder()
                    .setReservation(reservation)
                    .setSuccess(true)
                    .setMessage("Recurso recreativo reservado")
                    .build());
            responseObserver.onCompleted();
        }

        @Override
        public synchronized void getRecreationReservations(StudentRequest request,
                                                           StreamObserver<RecreationReservationList> responseObserver) {
            RecreationReservationList.Builder list = RecreationReservationList.newBuilder();
            for (RecreationReservation reservation : reservations) {
                if (reservation.getStudentId().equals(request.getStudentId())) {
                    list.addReservations(reservation);
                }
            }

            responseObserver.onNext(list.build());
            responseObserver.onCompleted();
        }
    }
}
