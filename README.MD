### InterSCSimulator - Scenario Tools

This project groups all the projects that aim to create a InterSCSimulator simulation scenario, such as the map and subway graph, the set of trips, and the buses.

Most of the projects receive a input file with the required information and generates an XML or CSV in the InterSCSimulator format.


## Travel Generator


## Vanet Generator

The Vanet genarator is a tool that receive the trace file of InterSCSimulator and generates the trace file used to simulate VANETs in the NS-3 simulator.

The classes required to this tool are:

- Connector.java
- VanetGenerator.java
- Car.java