package edu.eci.arsw.wellness.gateway;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class WellnessGateway implements AutoCloseable {
    private final ManagedChannel appointmentChannel;
    private final ManagedChannel medicalChannel;
    private final ManagedChannel gymChannel;
    private final ManagedChannel recreationChannel;
    private final AppointmentServiceGrpc.AppointmentServiceBlockingStub appointmentStub;
    private final MedicalServiceGrpc.MedicalServiceBlockingStub medicalStub;
    private final GymServiceGrpc.GymServiceBlockingStub gymStub;
    private final RecreationServiceGrpc.RecreationServiceBlockingStub recreationStub;

    public WellnessGateway() {
        appointmentChannel = channel(51051);
        medicalChannel = channel(51052);
        gymChannel = channel(51053);
        recreationChannel = channel(51054);

        appointmentStub = AppointmentServiceGrpc.newBlockingStub(appointmentChannel);
        medicalStub = MedicalServiceGrpc.newBlockingStub(medicalChannel);
        gymStub = GymServiceGrpc.newBlockingStub(gymChannel);
        recreationStub = RecreationServiceGrpc.newBlockingStub(recreationChannel);
    }

    public AppointmentResponse requestAppointment(String studentId, ServiceType serviceType) {
        return appointmentStub.requestAppointment(AppointmentRequest.newBuilder()
                .setStudentId(studentId)
                .setServiceType(serviceType)
                .setDate("POR_DEFINIR")
                .build());
    }

    public String getStudentWellnessSummary(String studentId) {
        AppointmentList appointments = appointmentStub.getAppointments(StudentRequest.newBuilder()
                .setStudentId(studentId)
                .build());
        GymReservationList gymReservations = gymStub.getGymReservations(StudentRequest.newBuilder()
                .setStudentId(studentId)
                .build());
        RecreationReservationList recreationReservations =
                recreationStub.getRecreationReservations(StudentRequest.newBuilder()
                        .setStudentId(studentId)
                        .build());
        SpecialtyList specialties = medicalStub.listSpecialties(Empty.newBuilder().build());

        StringBuilder summary = new StringBuilder();
        summary.append("Resumen de bienestar para ").append(studentId).append(System.lineSeparator());
        summary.append("Citas activas: ").append(appointments.getAppointmentsCount()).append(System.lineSeparator());
        summary.append("Reservas de gimnasio: ").append(gymReservations.getReservationsCount()).append(System.lineSeparator());
        summary.append("Reservas recreativas: ").append(recreationReservations.getReservationsCount()).append(System.lineSeparator());
        summary.append("Especialidades disponibles: ").append(specialties.getSpecialtiesCount());
        return summary.toString();
    }

    public GymReservationResponse reserveGymSession(String studentId, String timeSlot) {
        return gymStub.reserveGymSession(GymReservationRequest.newBuilder()
                .setStudentId(studentId)
                .setTimeSlot(timeSlot)
                .build());
    }

    public RecreationReservationResponse reserveRecreationResource(String studentId, String resourceId) {
        return recreationStub.reserveRecreationResource(RecreationReservationRequest.newBuilder()
                .setStudentId(studentId)
                .setResourceId(resourceId)
                .build());
    }

    @Override
    public void close() {
        appointmentChannel.shutdown();
        medicalChannel.shutdown();
        gymChannel.shutdown();
        recreationChannel.shutdown();
    }

    public static void main(String[] args) {
        String studentId = "20261001";

        try (WellnessGateway gateway = new WellnessGateway()) {
            AppointmentResponse appointment =
                    gateway.requestAppointment(studentId, ServiceType.PSYCHOLOGY);
            System.out.println("requestAppointment: " + appointment.getMessage());

            GymReservationResponse gym =
                    gateway.reserveGymSession(studentId, "2026-06-17 18:00");
            System.out.println("reserveGymSession: " + gym.getMessage());

            RecreationReservationResponse recreation =
                    gateway.reserveRecreationResource(studentId, "CHESS-01");
            System.out.println("reserveRecreationResource: " + recreation.getMessage());

            System.out.println(gateway.getStudentWellnessSummary(studentId));
        }
    }

    private ManagedChannel channel(int port) {
        return ManagedChannelBuilder
                .forAddress("localhost", port)
                .usePlaintext()
                .build();
    }
}
