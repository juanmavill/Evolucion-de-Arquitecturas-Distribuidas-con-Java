package edu.eci.arsw.wellness.ms;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.util.EnumMap;
import java.util.Map;

public class MedicalServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(51052)
                .addService(new MedicalServiceImpl())
                .build();

        server.start();
        System.out.println("MedicalService iniciado en puerto 51052");
        server.awaitTermination();
    }

    static class MedicalServiceImpl extends MedicalServiceGrpc.MedicalServiceImplBase {
        private final Map<ServiceType, Specialty> specialties = new EnumMap<>(ServiceType.class);

        public MedicalServiceImpl() {
            specialties.put(ServiceType.MEDICINE, Specialty.newBuilder()
                    .setServiceType(ServiceType.MEDICINE)
                    .setName("Medicina general")
                    .setLocation("Bloque Bienestar")
                    .setAvailable(true)
                    .build());
            specialties.put(ServiceType.PSYCHOLOGY, Specialty.newBuilder()
                    .setServiceType(ServiceType.PSYCHOLOGY)
                    .setName("Psicologia")
                    .setLocation("Consultorio 204")
                    .setAvailable(true)
                    .build());
            specialties.put(ServiceType.DENTISTRY, Specialty.newBuilder()
                    .setServiceType(ServiceType.DENTISTRY)
                    .setName("Odontologia")
                    .setLocation("Consultorio 101")
                    .setAvailable(true)
                    .build());
        }

        @Override
        public void listSpecialties(Empty request,
                                    StreamObserver<SpecialtyList> responseObserver) {
            SpecialtyList.Builder list = SpecialtyList.newBuilder();
            for (Specialty specialty : specialties.values()) {
                list.addSpecialties(specialty);
            }

            responseObserver.onNext(list.build());
            responseObserver.onCompleted();
        }

        @Override
        public void getSpecialty(SpecialtyRequest request,
                                 StreamObserver<SpecialtyResponse> responseObserver) {
            Specialty specialty = specialties.get(request.getServiceType());

            SpecialtyResponse response = specialty == null
                    ? SpecialtyResponse.newBuilder().setFound(false).build()
                    : SpecialtyResponse.newBuilder().setSpecialty(specialty).setFound(true).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}
