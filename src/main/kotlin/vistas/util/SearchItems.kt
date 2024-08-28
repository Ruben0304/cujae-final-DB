package vistas.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import vistas.Consulta
import vistas.SearchItem
import vistas.Turno
import vistas.componentes.InfoItem

object SearchItems {
    val allItems = listOf(
        SearchItem(
            type = "Doctor",
            name = "Mario Alfonso Perez Alvarez",
            id = "0405060707",
            gradient = Colores.doctorGradient,
            avatar = "a.jpg",
            infoItems = listOf(
                InfoItem(Icons.Rounded.Badge, "Número de Licencia", "12345"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-1234"),
                InfoItem(Icons.Rounded.LockClock, "Años de Experiencia", "10"),
                InfoItem(Icons.Rounded.Email, "Datos de Contacto", "doctor@example.com")
            )
        ),
        SearchItem(
            type = "Paciente",
            name = "Gerard Martin",
            id = "0405060708",
            gradient = Colores.patientGradient,
            avatar = "Untitled.png",
            infoItems = listOf(
                InfoItem(Icons.Rounded.Badge, "Número de Historia", "67890"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-5678"),
                InfoItem(Icons.Rounded.CalendarToday, "Fecha de Nacimiento", "01/01/1990"),
                InfoItem(Icons.Rounded.Email, "Correo Electrónico", "paciente@example.com")
            )
        ),
        SearchItem(
            type = "Hospital",
            name = "Hospital General",
            id = "H001",
            gradient = Colores.hospitalGradient,
            avatar = "Hospital Sign.png",
            infoItems = listOf(
                InfoItem(Icons.Rounded.LocationOn, "Dirección", "Calle Principal 123"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-9876"),
                InfoItem(Icons.Rounded.Bedtime, "Número de Camas", "200"),
                InfoItem(Icons.Rounded.MedicalServices, "Especialidades", "10")
            )
        ), SearchItem(
            type = "Doctor",
            name = "Mario Alfonso Perez Alvarez",
            id = "0405060707",
            gradient = Colores.doctorGradient,
            avatar = "a.jpg",
            infoItems = listOf(
                InfoItem(Icons.Rounded.Badge, "Número de Licencia", "12345"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-1234"),
                InfoItem(Icons.Rounded.LockClock, "Años de Experiencia", "10"),
                InfoItem(Icons.Rounded.Email, "Datos de Contacto", "doctor@example.com")
            )
        ),
        SearchItem(
            type = "Paciente",
            name = "Gerard Martin",
            id = "0405060708",
            gradient = Colores.patientGradient,
            avatar = "Untitled.png",
            infoItems = listOf(
                InfoItem(Icons.Rounded.Badge, "Número de Historia", "67890"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-5678"),
                InfoItem(Icons.Rounded.CalendarToday, "Fecha de Nacimiento", "01/01/1990"),
                InfoItem(Icons.Rounded.Email, "Correo Electrónico", "paciente@example.com")
            )
        ),
        SearchItem(
            type = "Hospital",
            name = "Hospital General",
            id = "H001",
            gradient = Colores.hospitalGradient,
            avatar = "Hospital Sign.png",
            infoItems = listOf(
                InfoItem(Icons.Rounded.LocationOn, "Dirección", "Calle Principal 123"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-9876"),
                InfoItem(Icons.Rounded.Bedtime, "Número de Camas", "200"),
                InfoItem(Icons.Rounded.MedicalServices, "Especialidades", "10")
            )
        ), SearchItem(
            type = "Doctor",
            name = "Mario Alfonso Perez Alvarez",
            id = "0405060707",
            gradient = Colores.doctorGradient,
            avatar = "a.jpg",
            infoItems = listOf(
                InfoItem(Icons.Rounded.Badge, "Número de Licencia", "12345"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-1234"),
                InfoItem(Icons.Rounded.LockClock, "Años de Experiencia", "10"),
                InfoItem(Icons.Rounded.Email, "Datos de Contacto", "doctor@example.com")
            )
        ),
        SearchItem(
            type = "Paciente",
            name = "Gerard Martin",
            id = "0405060708",
            gradient = Colores.patientGradient,
            avatar = "Untitled.png",
            infoItems = listOf(
                InfoItem(Icons.Rounded.Badge, "Número de Historia", "67890"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-5678"),
                InfoItem(Icons.Rounded.CalendarToday, "Fecha de Nacimiento", "01/01/1990"),
                InfoItem(Icons.Rounded.Email, "Correo Electrónico", "paciente@example.com")
            )
        ),
        SearchItem(
            type = "Hospital",
            name = "Hospital General",
            id = "H001",
            gradient = Colores.hospitalGradient,
            avatar = "Hospital Sign.png",
            infoItems = listOf(
                InfoItem(Icons.Rounded.LocationOn, "Dirección", "Calle Principal 123"),
                InfoItem(Icons.Rounded.Phone, "Teléfono", "555-9876"),
                InfoItem(Icons.Rounded.Bedtime, "Número de Camas", "200"),
                InfoItem(Icons.Rounded.MedicalServices, "Especialidades", "10")
            )
        )
    )


    val turnos = listOf(
        Turno("1", 60, 40, "Mario Alfonso Perez Alvarez"),
        Turno("2", 30, 28, "Mario Alfonso Perez Alvarez"),
        Turno("3", 20, 8),
        Turno("1", 60, 40),

        )

    val consultas = listOf(
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez", false),
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez", false),
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez"),
        Consulta("1", "Carlos Alberto", "Mario Sanchez", false),

        )
}