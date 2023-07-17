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

![Build](https://img.shields.io/badge/Build-0.1.0--alpha-blue.svg)
[![Qodana](https://github.com/mcxross/cohesive/actions/workflows/code_quality.yml/badge.svg)](https://github.com/mcxross/cohesive/actions/workflows/code_quality.yml)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
<hr />

> Convention: In this document, we will use the terms 'platform' and 'blockchain' interchangeably, with the term 'chain' occasionally used as well.

# Why?

Blockchain Development introduces a new development paradigm that requires a new set of tools and workflows i.e. Explorer, Wallet and Editor (IDE). Context switching between these tools is expensive in terms of time and space which is a major productivity killer. Cohesive is a single application that provides a unified development environment for Blockchain development.

# How?
 Cohesive is a meta-tool for developing blockchain applications. It offers a UI framework, plugin store, simple editor, and IDE for easy and efficient development on multiple platforms.

## Table of contents
- [Quick start](#quick-start)
- [Build locally](#build-locally)
- [What's included](#whats-included)
- [Features](#features)
- [Roadmap](#roadmap)
- [Design Philosophy](#design-philosophy)
- [Contribution](#contribution)

**Note:** This project is in its early stages of development. The project is not yet ready for production use.

## Quick Start

### Build a Secondary Plugin for a Specific Platform (Blockchain)

The quickest way to get started is to use this [template](https://github.com/mcxross/x-cohesive) project. It contains all the necessary files and configurations to get you started.

Cohesive uses _encapsulated slot APIs_, a pattern akin to Compose' Material components' _slot APIs_ for its customization. Slots leave gaps in the UI for the developer to fill as they wish for the specific Platform implementation. The template project contains a sample implementation of the _encapsulated slot APIs_ for the Ethereum Platform.

```kotlin
/**
 * This code defines a [MainView] class that extends the [CohesiveView] class and is annotated with the [Cohesive] annotation.
 * The [Cohesive] annotation takes three arguments:
 *
 * - [platform]: a string specifying the platform that the view is designed for. In this case, the platform is "Ethereum".
 * - [version]: a string specifying the version of the platform. In this case, the version is "1.0.0".
 * - [nets]: an array of [Net] objects, each of which specifies a network and its corresponding URL.
 *   In this case, there are two networks specified: "mainnet" and "ropsten".
 *
 * The [MainView] class also has four abstract methods: [Explorer], [Wallet] and others, which can be implemented by any class that extends [MainView].
 * These methods are annotated with the [Composable] annotation, which indicates that they are meant to be used with the Compose framework.
 */
@Cohesive(
  platform = "Ethereum",
  version = "1.0.0",
  nets = 
    [
      Net(
        k = "mainnet",
        v = "https://mainnet.infura.io/v3/your-api-key"
      ), 
      Net(
        k = "ropsten",
        v = "https://ropsten.infura.io/v3/your-api-key"
      ),
    ],
)
class MainView : CohesiveView {
    @Composable
    override fun Explorer() {
        //TODO Your Ethereum Explorer implementation goes here
    }
    @Composable
    override fun Wallet() {
        //TODO Your Ethereum Wallet implementation goes here
    }
}


``` 
More information can be got [here](https://mcxross.github.io/cohesive-doc/).


## Build locally

### Running Base Desktop Application
```
./gradlew :cmpe:desktop:run
```

#### Building Base native desktop distribution
```
./gradlew :cmpe:desktop:package
# outputs are written to cmpe/desktop/build/compose/binaries
```

### Running Base Web Application
```
./gradlew :cmpe:web:jsBrowserRun
```

#### Distribute Base Web Application
```
./gradlew :cmpe:web:jsBrowserDistribution 
# and then open index.html (build/distributions)
```

## What's included
This repository contains a Kotlin Multiplatform project with apps and libraries for multiple platforms:

* Web Application
* Desktop Application
* Android Application
* iOS Application (WIP)

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
All contributions to Cohesive are welcome. Before opening a PR, please submit an issue detailing the bug or feature. When opening a PR, please ensure that your contribution builds on the KMM toolchain, has been linted with `ktfmt <GOOGLE (INTERNAL)>`, and contains tests when applicable. For more information, please see the [contribution guidelines](CONTRIBUTING.md).
