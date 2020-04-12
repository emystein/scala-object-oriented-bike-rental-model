package ar.com.flow.bikerental.model

import ar.com.flow.bikerental.model.trip.completion.TripCompletionRules

case class Trip(pickUp: BikePickUpEvent, completionRules: TripCompletionRules)
