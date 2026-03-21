package projects.kankan.projects.kankan.repository

import org.springframework.data.jpa.repository.JpaRepository
import projects.kankan.model.Card

interface CardRepository: JpaRepository<Card, Long>