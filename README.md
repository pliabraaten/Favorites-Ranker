# Favorites Ranker

Rank anything using pairwise comparisons.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Demo](#demo)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Algorithm Design](#algorithm-design)
- [Future_Optimizations](#future-optimizations) 


## Overview

Favorites Ranker is a full-stack web application built with Spring Boot and Thymeleaf. Users can create personalized lists (favorite movies, TV shows, books, etc.) and rank items interactively.
Instead of users sorting the items of their lists manually, it uses a guided comparison system that determines optimal rankings using two methods:
- **Pairwise Comparison Algorithm**: Make head-to-head comparisons to generate rankings
- **Manual Positioning**: Use inline editing to adjust rankings manually

The application demonstrates modern full-stack development practices including RESTful API design, secure authentication, responsive UI, and CRUD functionality with relational persistence via JPA/Hibernate.


## Features

### Core Functionality
-  **User Authentication & Authorization** - Secure login/registration with Spring Security
-  **List Management** - Create, read, update, and delete custom ranking lists
-  **Binary Insertion Sort Algorithm** - Optimized O(n²) pairwise comparison ranking
-  **Inline Editing** - Edit list and item names directly without navigating to separate pages
-  **Manual Repositioning** - Adjust item positions with up/down arrow buttons
-  **Cascade Deletion** - Automatic position reindexing when items are removed


## Demo

> *Registering a user and logging in*
> ![Demo](docs/demos/registerUser.gif)
> *Adding items to a list individually and in batch*
> ![Demo](docs/demos/createItems.gif)
> *User prompted with pairwise comparisons*
> ![Demo](docs/demos/pairwiseComparisons.gif)
> *User making inline edits to the list and manually reranking an item*
> ![Demo](docs/demos/inlineEdits.gif)


##  Tech Stack

### Backend Development
- Spring Boot 3 + Java 17 — Application framework and runtime
- Spring Security — Authentication, session handling, and route protection
- Spring Data JPA (Hibernate) — Persistence layer and ORM mapping
- Embedded H2 Database — Relational data storage for development/runtime
- Service Layer Architecture — Separation of controller, service, and repository concerns
- Transactional Data Handling — Ensures integrity during list updates and ranking operations

### Frontend
- **Thymeleaf** - Server-side rendering and template composition
- **Bootstrap 5** - Responsive layout and UI components
- **JavaScript** - Client-side interaction logic and ranking algorithm execution
- **Bootstrap Icons** - UI visuals and controls


## Installation
Pre-built images are available on the project’s [GitHub Packages page](https://github.com/pliabraaten/Favorites-Ranker/pkgs/container/favorites-ranker). 
These images contain everything needed to run the application .

### Run instantly with Docker:
```bash
# Run with no data persistence:
docker run -p 8080:8080 ghcr.io/pliabraaten/favorites-ranker:latest
```
```bash
# Run with data persistence:
docker run -p 8080:8080 -v ./data:/app/data ghcr.io/pliabraaten/favorites-ranker:latest
```

### Run Locally with Docker Compose
```bash
# Clone and run
git clone https://github.com/pliabraaten/Favorites-Ranker.git
cd Favorites-Ranker
docker-compose up
# Access at http://localhost:8080
# Data Persistence: Your lists and rankings automatically save to the `./data` folder and persist between restarts.
```


## Algorithm Design

### Binary Insertion Ranking Strategy

The ranking system implements a **Binary Insertion** algorithm designed for user-driven pairwise comparisons. This approach minimizes the number of comparisons needed while providing an intuitive, interactive ranking experience.

### Implementation
```javascript
/**
 * Binary search to find optimal insertion position for ranked item
 * @param {number} i - Index of item being ranked
 * @param {number} L - Left boundary of search range
 * @param {number} R - Right boundary of search range
 * @param {Object} selectedItem - Item to insert into ranking
 */
function binarySearch(i, L, R, selectedItem) {

  // Base case: search range exhausted, position found
  if (L > R || R < 0) {
    // Insert item at the found position (L is the insertion index)
    insertItem(L, i, selectedItem); 
    moveToNextItem();
    return;
  }

  // Find middle element in the sorted portion of the array
  let M = L + Math.floor((R - L) / 2);

  // Prompt user to compare selectedItem with item at position M
  pairwisePrompt(i, M, function(winner) {
    if (winner.id === selectedItem.id) { 
      // Selected item ranked higher - search left half
      binarySearch(i, L, M - 1, selectedItem);
    } else {
      // Middle item ranked higher - search right half
      binarySearch(i, M + 1, R, selectedItem);
    }
  });
}
```

**Key Implementation Details:**
- **Recursive approach** handles JavaScript's asynchronous nature with user input callbacks
- **Callback pattern** (`pairwisePrompt`) waits for user selection before continuing search
- **Overflow prevention**: Uses `L + Math.floor((R - L) / 2)` instead of `(L + R) / 2`
- **Binary partitioning**: Eliminates half the search space with each comparison

### Complexity Analysis

| Operation | Complexity | Explanation |
|-----------|-----------|-------------|
| **Finding insertion position** | O(log n) | Binary search per item |
| **User comparisons (n items)** | O(n log n) | log n comparisons × n items |
| **Total insertion operations** | O(n²) | n insertions × O(n) shift per insert |
| **Overall algorithm** | **O(n²)** | Dominated by insertion cost |

#### Detailed Breakdown

**Comparison Phase: O(n log n)**
- Binary search finds the optimal position in **O(log n)** comparisons per item
- Ranking **n items** requires **O(n log n)** total user comparisons

**Insertion Phase: O(n²)**
- After finding position, must **shift array elements** to make space
- JavaScript array insertion at index requires shifting: **O(n)** per operation
- Performing **n insertions** results in: **O(n²)** total shifts

**Overall Complexity: O(n²)**
- While binary search minimizes *comparisons*, array manipulation dominates runtime
- **Trade-off**: Optimized for *user experience* (fewer questions) rather than *pure computational efficiency*

## Future Optimizations

Potential improvements:
- **ELO Rating System**: Assign dynamic scores for few comparisons
- **List Sharing**: Send lists to friends and compare rankings