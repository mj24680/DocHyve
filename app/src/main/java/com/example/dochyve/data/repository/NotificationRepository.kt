package com.example.dochyve.data.repository

import com.example.dochyve.data.model.Notification

class NotificationRepository {

    fun getNotifications() : List<Notification>{
        return listOf(

            Notification(
                id = 1,
                patientName = "John Doe",
                doctorName = "Dr. Irfan",
                date = "July 15, 2023",
                time = "2:00 PM",
                duration = "5 min ago"
            ),

            Notification(
                id = 2,
                patientName = "Ali Khan",
                doctorName = "Dr. Sarah",
                date = "July 16, 2023",
                time = "11:00 AM",
                duration = "10 min ago"
            ),

            Notification(
                id = 3,
                patientName = "Ahmed Raza",
                doctorName = "Dr. Fatima",
                date = "July 17, 2023",
                time = "9:30 AM",
                duration = "15 min ago"
            ),

            Notification(
                id = 4,
                patientName = "Michael Smith",
                doctorName = "Dr. Usman",
                date = "July 18, 2023",
                time = "4:00 PM",
                duration = "20 min ago"
            ),

            Notification(
                id = 5,
                patientName = "Sara Ahmed",
                doctorName = "Dr. Emily",
                date = "July 19, 2023",
                time = "1:15 PM",
                duration = "25 min ago"
            ),

            Notification(
                id = 6,
                patientName = "David Lee",
                doctorName = "Dr. Hassan",
                date = "July 20, 2023",
                time = "3:45 PM",
                duration = "30 min ago"
            ),

            Notification(
                id = 7,
                patientName = "Ayesha Noor",
                doctorName = "Dr. Bilal",
                date = "July 21, 2023",
                time = "10:00 AM",
                duration = "35 min ago"
            ),

            Notification(
                id = 8,
                patientName = "Chris Evans",
                doctorName = "Dr. Hina",
                date = "July 22, 2023",
                time = "5:30 PM",
                duration = "40 min ago"
            )
        )
    }
}