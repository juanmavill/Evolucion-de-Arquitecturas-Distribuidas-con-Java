package edu.eci.arsw.wellness.ms;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WellnessDirectClient {
    public static void main(String[] args) {
        ManagedChannel appointmentChannel = ManagedChannelBuilder
                .forAddress("localhost", 51051)
                .usePlaintext()
                .build();
        ManagedChannel medicalChannel = ManagedChannelBuilder
                .forAddress("localhost", 51052)
                .usePlaintext()
                .build();
        ManagedChannel gymChannel = ManagedChannelBuilder
                .forAddress("localhost", 51053)
                .usePlaintext()
                .build();
        ManagedChannel recreationChannel = ManagedChannelBuilder
                .forAddress("localhost", 51054)
                .usePlaintext()
                .build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub =
                AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub =
                MedicalServiceGrpc.newBlockingStub(medicalChannel);
        GymServiceGrpc.GymServiceBlockingStub gymStub =
                GymServiceGrpc.newBlockingStub(gymChannel);
        RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub =
                RecreationServiceGrpc.newBlockingStub(recreationChannel);

        String studentId = "20261001";

        AppointmentResponse appointment = appointmentStub.requestAppointment(
                AppointmentRequest.newBuilder()
                        .setStudentId(studentId)
                        .setServiceType(ServiceType.MEDICINE)
                        .setDate("2026-06-16 09:00")
                        .build());
        System.out.println("AppointmentService: " + appointment.getMessage());

        SpecialtyList specialties = medicalStub.listSpecialties(Empty.newBuilder().build());
        System.out.println("MedicalService especialidades: " + specialties.getSpecialtiesCount());

        GymReservationResponse gym = gymStub.reserveGymSession(
                GymReservationRequest.newBuilder()
                        .setStudentId(studentId)
                        .setTimeSlot("2026-06-16 18:00")
                        .build());
        System.out.println("GymService: " + gym.getMessage());

        RecreationReservationResponse recreation = recreationStub.reserveRecreationResource(
                RecreationReservationRequest.newBuilder()
                        .setStudentId(studentId)
                        .setResourceId("CHESS-01")
                        .build());
        System.out.println("RecreationService: " + recreation.getMessage());

        appointmentChannel.shutdown();
        medicalChannel.shutdown();
        gymChannel.shutdown();
        recreationChannel.shutdown();
    }
}
