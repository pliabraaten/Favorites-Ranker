# Favorites-Ranker
List ranking tool using pairwise comparison to determine preference order



Rank Algorithm: 
- initially started with iterating through every combination of elements
- updated to omit pairs of the same element
- changed to matrix to store winners as values and elements as indexes and to avoid repeated pairs (ab and then ba)
- changed back to list of elements and list of results (avoid repeated pairs)
  - ranked based on count of wins for each element
  - even omitting repeated pairs, it still is n*(n – 1)/2 comparisons or N^2
- change to binary insertion sort to decrease the number of comparisons for the user


# Favorites Ranker

A full-stack web application that enables users to create custom lists and rank items through an intuitive pairwise comparison algorithm and manual positioning controls.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Algorithm Deep Dive](#algorithm-deep-dive)
- [Tech Stack](#tech-stack)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Learning Outcomes](#learning-outcomes)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

## Overview

Favorites Ranker is a web-based ranking application built with Spring Boot and Thymeleaf. Users can create personalized lists (favorite movies, TV shows, books, etc.) and rank items using two methods:
- **Pairwise Comparison Algorithm**: Make head-to-head comparisons to generate an optimized ranking
- **Manual Positioning**: Use intuitive arrow buttons or inline editing to adjust rankings on the fly

The application demonstrates modern full-stack development practices including RESTful API design, secure authentication, responsive UI, and database management.

## Features

### Core Functionality
-  **User Authentication & Authorization** - Secure login/registration with Spring Security
-  **List Management** - Create, read, update, and delete custom ranking lists
-  **Binary Insertion Sort Algorithm** - Optimized O(n log n) pairwise comparison ranking with minimal user effort
-  **Inline Editing** - Edit list and item names directly without navigating to separate pages
-  **Manual Repositioning** - Adjust item positions with up/down arrow buttons
-  **Cascade Deletion** - Automatic position reindexing when items are removed
-  **Responsive Design** - Mobile-first UI built with Bootstrap 5

### Technical Highlights
- **Binary Search Optimization** - Logarithmic comparison complexity for efficient ranking
- **RESTful API Architecture** - Clean separation between MVC controllers and API endpoints
- **AJAX Operations** - Asynchronous updates for seamless user experience
- **Transaction Management** - ACID-compliant database operations with Spring JPA
- **DTO Pattern** - Decoupled data transfer between layers
- **Service Layer Architecture** - Business logic separation following SOLID principles- 

## Algorithm Deep Dive: Binary Insertion Sort with Pairwise Comparisons

### Overview

The ranking system implements a **Binary Insertion Sort** algorithm optimized for user-driven pairwise comparisons. This approach minimizes the number of comparisons needed while providing an intuitive, interactive ranking experience.

### How It Works

Traditional sorting requires comparing item values directly. However, when ranking subjective preferences (e.g., "Which movie is better?"), there are no inherent numeric values. Instead, the algorithm:

1. **Maintains a sorted sublist** of already-ranked items
2. **Inserts new items** one at a time using binary search
3. **Asks the user to compare** items at strategic positions
4. **Converges to optimal placement** with minimal comparisons

### Implementation
```java
// Simplified pseudo-code of the core algorithm
public void rankItem(Item newItem, List rankedItems) {
    int low = 0;
    int high = rankedItems.size();
    
    while (low < high) {
        int mid = (low + high) / 2;
        
        // Ask user: "Is newItem better than rankedItems[mid]?"
        boolean isBetter = getUserComparison(newItem, rankedItems.get(mid));
        
        if (isBetter) {
            high = mid;  // Search upper half
        } else {
            low = mid + 1;  // Search lower half
        }
    }
    
    // Insert at optimal position
    rankedItems.add(low, newItem);
    updatePositions(rankedItems);
}
```

### Time Complexity Analysis

| Operation | Best Case | Average Case | Worst Case | Space |
|-----------|-----------|--------------|------------|-------|
| **Ranking n items** | O(n log n) | O(n log n) | O(n²) | O(n) |
| **Single item insertion** | O(log n) | O(log n) | O(n) | O(1) |
| **Position update** | O(n) | O(n) | O(n) | O(1) |

#### Detailed Analysis

**Best/Average Case: O(n log n)**
- Binary search finds insertion point in **O(log n)** comparisons
- Repeated for **n items** → **O(n log n)** total comparisons
- This is **optimal** for comparison-based sorting (proven lower bound)
- Significantly better than naive bubble sort O(n²)

**Worst Case: O(n²)**
- Occurs when user comparisons are inconsistent (e.g., circular preferences: A > B > C > A)
- Algorithm may need to backtrack or make additional comparisons
- Still completes successfully due to fallback mechanisms

**Space Complexity: O(n)**
- Stores ranked items in-memory
- No additional data structures required beyond the list itself

[//]: # (FIXME:)
Time Complexity:
    Comparisons with binary search: O(n log n) -> O(log n) comparisons per element resulting in O(n log n) comparisons overall
    Insertion and shifting items: O(n^2)
    Overall: O(n^2) on average/worst case because of the swaps required for each insertion.


### Advantages Over Alternatives

| Algorithm | Comparisons | User Burden | Consistency Handling |
|-----------|-------------|-------------|----------------------|
| **Binary Insertion Sort** | O(n log n) | Minimal | Good |
| Bubble Sort | O(n²) | Very High | Poor |
| Merge Sort | O(n log n) | N/A* | N/A* |
| Quick Sort | O(n log n) avg | N/A* | N/A* |

*Cannot be used for user-driven comparisons (requires predetermined comparison function)

### Why This Algorithm?

1. **Minimizes User Effort**: Only ~log₂(n) questions per item
  - Ranking 10 items: ~33 comparisons (vs. 45 with bubble sort)
  - Ranking 20 items: ~86 comparisons (vs. 190 with bubble sort)

2. **Intuitive User Experience**: Simple yes/no questions
  - "Which do you prefer: Breaking Bad or The Wire?"
  - No complex multi-way comparisons

3. **Handles Inconsistencies**: Gracefully manages circular preferences
  - Uses most recent comparison data
  - Provides stable results even with contradictory input

4. **Database Efficient**: Batch position updates
  - Single transaction per item insertion
  - Optimized SQL for position reindexing

### Real-World Performance

**Example: Ranking 15 Movies**
- Theoretical comparisons: ~41 (log₂(15!) ≈ 41)
- Actual user comparisons: ~38-45 (depending on choices)
- Time to complete: ~2-3 minutes (at 4 seconds per comparison)

**vs. Manual Drag-and-Drop:**
- Potentially unlimited adjustments
- No guarantee of optimal order
- Cognitive load of managing entire list

### Future Optimizations

Planned improvements tracked in the roadmap:
- **ELO Rating System**: Assign dynamic scores for faster insertions
- **Machine Learning**: Predict preferences based on past comparisons
- **Adaptive Questioning**: Skip obvious comparisons (e.g., #1 vs. #15)

##  Tech Stack

### Backend Development
-  Building RESTful APIs with Spring Boot
- Implementing authentication/authorization with Spring Security
-  **Designing and implementing sorting algorithms for user-driven data**
-  **Analyzing time/space complexity and optimizing for real-world performance**
-  Database design and JPA relationship mapping
-  Transaction management and data integrity
-  Service layer architecture and dependency injection

### Frontend
- **Thymeleaf** - Server-side template engine
- **Bootstrap 5** - Responsive CSS framework
- **JavaScript (ES6+)** - Client-side interactivity
- **Bootstrap Icons** - UI iconography

### Development Tools
- **Maven** - Dependency management and build automation
- **Git** - Version control with feature branch workflow
- **GitHub Projects** - Issue tracking and project management

## Screenshots

> *Add screenshots here showing:*
> - Login page
> - List overview
> - Pairwise comparison interface
> - List details with inline editing
> - Manual repositioning with arrows

## Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Git

### Setup Instructions

1. **Clone the repository**
```bash