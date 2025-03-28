1. Separate user details from auth details
   If user has registered via oidc in user_auth should appear a record with this user's oidc login details and app_user
   with their user details, if user sets password and login after being registered via oidc a new record in user_auth
   will appear with auth_type PASSWORD
2. design problem to solve

* user should be able to create a new post and see list of posts
* user can answer a post or answer a post mentioning another answer(answering an answer)
* user can upvote or downvote both posts and answers
* when looking at the list of post user can sort by latest date or highest upvotes
* assume adding UI in the future user should be able to get new answers to specific post in realtime
* important concern is performance under load and scalability(assume we have thousands of posts with hundreds of votes
  and answers)

Example: Voting on a Post
Main intent: store the user’s vote in the DB
Side-effects:
Recalculate the vote count
Notify UI clients via WebSocket
Log the event for audit
Update recommendation score

3. Design plan

* Layered Architecture (Structural Foundation)
  Base structure that separates concerns:
  Controller
  Service
  DAO
  to enforce modularity and testability

* Event-Driven
  Used within the service layer to decouple operations:
  e.g., voting triggers a VoteSubmittedEvent
  Listeners react: update vote count, send real-time push
  Improves scalability, reduces service coupling
  
* CQRS-lite
  Logical separation:
  Commands (write actions): createPost(), voteAnswer()
  Queries (read actions): getPostsSortedByVotes()
  no need in full-blown CQRS/event sourcing - just separate read-optimized queries from write-side logic
  Enables flexible views (e.g., can denormallize post listing with votes)

4. 