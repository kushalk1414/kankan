package org.example.projects.kankan.model

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

data class Card( @Id
                 @GeneratedValue(strategy = GenerationType.IDENTITY)
                 val id: Long = 0,

                 @Column(unique = true, nullable = false)
                 val title: String,

                 val createdAt: LocalDateTime = LocalDateTime.now())
