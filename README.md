<p align="center">
    <img src="artwork/readme/cohesive-logo.png" alt="Cohesive logo" width="100" height="100">
</p>

<h2 align="center">Cohesive</h2>

<p align="center">
  Sleek, intuitive, and powerful Blockchain Integrated Development, and Analysis Environment.
  <br>
<b>Economics</b> | <b>Ergonomics</b> | <b>Aesthetics</b>
</p>

<img src="artwork/readme/preview.png" alt="Cohesive Preview" width="100%" height="100%">

## Table of contents
- [Quick start](#quick-start)
- [Status](#status)
- [What's included](#whats-included)
- [Documentation](#documentation)
- [Features](#features)
- [Roadmap](#roadmap)
- [Design Philosophy](#design-philosophy)
- [Contribution](#contribution)

**Note:** This project is in its early stages of development. The project is not yet ready for production use.

## Quick start
> Convention: We'll use "Platform" and "Blockchain" interchangeably herein - and rarely, "Chain"

Cohesive is a modular meta-tool, i.e., a tool for tools specifically for Blockchain technology. It's Multiplatform, and Blockchain-neutral with a base implementation of/and, core functionality including but not limited to UI framework, Plugin Store, Simple Editor, and IDE. Specific Platform implementation can be built and installed as a _secondary_ plugin acting as the Platform wrapper, i.e., consuming Cohesive, and thus bending its gears for that specific Platform.

### Running Base desktop application
```
./gradlew :desktop:run
```

### Building Base native desktop distribution
```
./gradlew :desktop:package
# outputs are written to desktop/build/compose/binaries
```
## Status

[![Qodana](https://github.com/mcxross/cohesive/actions/workflows/code_quality.yml/badge.svg)](https://github.com/mcxross/cohesive/actions/workflows/code_quality.yml)

## What's included
Above everything, this repo is a Kotlin Multiplatform project containing apps and libraries for the following platforms:

* Web Application
* Desktop Application
* Android Application
* iOS Application (WIP)

## Documentation


## Features
- [x] Multiplatform
- [x] Multi-Blockchain
- [x] Platform Plugin Store (primary, secondary and tertiary)
- [x] Simple Editor
- [x] IDE
- [x] UI Framework(s)

## Roadmap
_Always building, always improving._
## Design Philosophy
Simple, intuitive, and powerful.
 
## Contribution
