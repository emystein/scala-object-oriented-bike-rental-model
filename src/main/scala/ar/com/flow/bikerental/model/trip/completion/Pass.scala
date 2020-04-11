package ar.com.flow.bikerental.model.trip.completion

case class Pass() extends AfterTripAction {
  override def execute = new SuccessResult
}