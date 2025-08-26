package com.icinemas.enums;

public enum  NotificationType {

        BOOKING_CONFIRMED("I-Cinemas Show Booking Confirmed", "booking-confirmation", "Your I-Cinemas show booking has been confirmed, enjoy the show !!"),
        BOOKING_CANCELLED("I-Cinemas Show Booking Cancelled", "booking-cancelled", "Your booking has been cancelled"),
        PAYMENT_SUCCESS("I-Cinemas Show Payment Successful", "payment-success", "Your payment was successful"),
        PAYMENT_FAILED("I-Cinemas Show Payment Failed", "payment-failed", "Your payment failed"),
        REMINDER("I-Cinemas Show Booking Reminder", "reminder", "Reminder for your upcoming booking");

        private final String subject;
        private final String templateName;
        private final String defaultMessage;

        NotificationType(String subject, String templateName, String defaultMessage) {
            this.subject = subject;
            this.templateName = templateName;
            this.defaultMessage = defaultMessage;
        }

        public String getSubject() {
            return subject;
        }

        public String getTemplateName() {
            return templateName;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }
    }

