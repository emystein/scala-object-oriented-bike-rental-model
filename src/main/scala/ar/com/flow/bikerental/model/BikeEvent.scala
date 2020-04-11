package ar.com.flow.bikerental.model

import java.time.LocalDateTime

trait BikeEvent {
  def user: User
  def bike: Bike
  def timestamp: LocalDateTime
}