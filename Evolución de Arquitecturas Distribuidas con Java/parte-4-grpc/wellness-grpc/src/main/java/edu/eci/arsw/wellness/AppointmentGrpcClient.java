package edu.eci.arsw.wellness;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AppointmentGrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        AppointmentServiceGrpc.AppointmentServiceBlockingStub stub =
                AppointmentServiceGrpc.newBlockingStub(channel);

        Student student = Student.newBuilder()
                .setId("20261001")
                .setName("Ana Ruiz")
                .setInstitutionalEmail("ana.ruiz@escuelaing.edu.co")
                .build();

        AppointmentResponse created = stub.requestAppointment(AppointmentRequest.newBuilder()
                .setStudent(student)
                .setServiceType(ServiceType.PSYCHOLOGY)
                .setDate("2026-06-15 10:00")
                .build());

        System.out.println("Solicitud: " + created.getMessage()
                + " " + created.getAppointment().getId()
                + " " + created.getAppointment().getStatus());

        AppointmentList activeAppointments = stub.getAppointments(StudentRequest.newBuilder()
                .setStudentId(student.getId())
                .build());
        System.out.println("Citas activas: " + activeAppointments.getAppointmentsCount());

        CancelResponse cancelled = stub.cancelAppointment(CancelRequest.newBuilder()
                .setAppointmentId(created.getAppointment().getId())
                .setStudentId(student.getId())
                .build());
        System.out.println("Cancelacion: " + cancelled.getMessage());

        AppointmentList afterCancel = stub.getAppointments(StudentRequest.newBuilder()
                .setStudentId(student.getId())
                .build());
        System.out.println("Citas activas despues de cancelar: "
                + afterCancel.getAppointmentsCount());

        channel.shutdown();
    }
}
