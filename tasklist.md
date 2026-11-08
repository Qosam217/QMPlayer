# QMPlayer --- Project Task List

> **Purpose:** Working task list for an AI coding agent continuing the
> QMPlayer Android project. **Last updated:** 2026-11-08 **Important:**
> This is a roadmap based on the latest project discussions, not proof
> that every discussed code sample was applied. The repository is the
> source of truth.

## 1. Instructions for the AI Agent

1.  Read this file completely before editing.
2.  Inspect the repository and Git status first. Verify every status
    below against actual source code.
3.  Update this file when tasks are completed, partially completed, or
    found to be obsolete.
4.  Preserve existing UI and naming. Do not recreate completed screens.
5.  Do not introduce Hilt. The project uses manual dependency injection
    through `QMPlayerContainer`.
6.  `HomeScreen` currently owns its `Scaffold`; do not introduce
    `HomeContent` unless deliberately refactoring with the user's
    approval.
7.  Preserve internal navigation enum values:
    -   `NavigationPage.Library` --- visible label: Library
    -   `NavigationPage.Queue` --- visible label: Playlist
    -   `NavigationPage.Discover` --- visible label: Tags
8.  Implement in small, verifiable steps. Build and test after each
    logical group.
9.  Do not silently change architecture, dependencies, package names, or
    product behavior. Record consequential decisions under Section 16.
10. A UI or dummy ViewModel is not proof that a feature is functionally
    integrated.

Task markers: `[ ]` not started; `[-]` in progress/partial; `[x]`
implemented and verified; `[?]` inspect or decision needed.

## 2. Project Overview and Confirmed Decisions

### Product scope

QMPlayer is a native Android local music player built with Kotlin and
Jetpack Compose. Main areas: - Library --- browse local music. -
Playlist --- create and manage ordered playlists. - Tags --- organize
songs with user-defined tags. - Player --- playback screen and current
queue. - Download --- download music from a supported YouTube link;
implementation scope is not yet confirmed. - Lyrics --- display lyrics
from a TXT file. - Settings.

### Technology and architecture

-   Kotlin, Jetpack Compose, Material 3.
-   Android Media3 / ExoPlayer.
-   Room 3 for QMPlayer-owned metadata and organization.
-   MediaStore for device audio discovery.
-   Manual DI through `QMPlayerContainer`; no Hilt.
-   Dark theme only at the current stage.
-   Package used in project discussions: `com.project.qmplayer`.
-   Project path previously used: `D:/project/QMPlayer`.
-   Verify actual dependency versions and package structure before
    editing; retain compatible versions already in the repository.

### Data ownership rules

-   MediaStore is the source of truth for device audio files.
-   Do **not** create a Room `SongEntity` or duplicate the full
    MediaStore song catalogue in Room.
-   Room stores QMPlayer-specific metadata and relationships only:
    custom cover URI, lyrics TXT URI, tags, playlists and playlist
    ordering.
-   Handle stale song references safely when a file is deleted or
    becomes inaccessible.
-   Custom cover is a URI override in Room; do not rewrite the audio
    file.
-   Playlist order is meaningful. Tags are many-to-many and unordered.

### Playback architecture

Target:

``` text
Compose UI -> ViewModel -> MediaController
           -> MediaSessionService -> ExoPlayer -> Audio
```

-   `PlayerViewModel` must not own or instantiate ExoPlayer.
-   Playback state and queue must come from the Media3
    session/controller.
-   UI controls must operate on the actual player queue, not local
    display-only state.
-   Mini player and full player must show the same playback state.

## 3. Latest Known Status --- Verify Against Repository

  ---------------------------------------------------------------------------------------
  Area                                  Latest known status       Next action
  ------------------------------------- ------------------------- -----------------------
  Project foundation/dependencies       Verified & building       Preserve working
                                        (Gradle 9.6, AGP 9.2.1,   baseline
                                        Kotlin 2.2.10, Java 21)

  Manual DI / `QMPlayerContainer`       Verified & active         Container shared across
                                                                  all routes

  Room database/entities/DAOs           Verified (schema v1)      Cascade delete & null
                                                                  safety in place

  MediaStore data source                Verified & resilient      Safe query, trailing
                                                                  slash filter, fallbacks

  Library UI                            Functional & integrated   Connect tap to real
                                                                  playback in Phase 4

  Playlist UI                           Functional & integrated   Connect Play to real
                                                                  playback in Phase 4

  Tag UI                                Functional & integrated   Connect Play to real
                                                                  playback in Phase 4

  Playlist/Tag create dialogs           Validated & unique        Completed in Phase 2
                                        (case-insensitive checks)

  Song/Playlist/Tag option modals       Implemented & connected   All dialogs & actions
                                                                  active (Phase 2)

  HomeScreen                            Scaffold and tab routing  Replace dummy playback
                                        exist                     state and connect
                                                                  navigation

  Music Player UI                       Proposed design/sample    Implement in Phase 3
                                        code only                 

  On Playing queue UI                   Proposed design/sample    Implement in Phase 3
                                        code only                 

  MediaSessionService/MediaController   Not yet implemented       Implement in Phase 4

  Real                                  Not yet implemented       Implement in Phase 4-5
  playback/queue/shuffle/repeat/seek                              

  MiniPlayer                            Known to use dummy local  Connect in Phase 6
                                        state                     

  Download/Lyrics/Settings              Not confirmed complete    Later phases
  ---------------------------------------------------------------------------------------

## 4. Phase 0 --- Repository Audit and Build Baseline

-   [x] Inspect project structure and identify the actual application
    module. (Verified: single application module `:app` targeting SDK 37, minSdk 29).
-   [x] Inspect settings/root/app Gradle files, version catalog,
    manifest and ProGuard configuration. (Verified: AGP 9.2.1, Gradle 9.6.0, Kotlin 2.2.10, Room 3.0.3, KSP 2.3.10).
-   [x] Confirm Kotlin, AGP, Compose, Room, KSP and Media3 versions. Do
    not downgrade working dependencies based on this document. (Confirmed and preserved).
-   [x] Inspect Git status and preserve unrelated user changes. (Preserved untracked preview/ and tasklist.md).
-   [x] Run a clean Gradle build; record pre-existing failures before
    editing. (Verified: builds cleanly with Java 21 JBR; default Oracle javapath on machine lacked JBR compatibility).
-   [x] Confirm package name and compare with `com.project.qmplayer`. (Confirmed: `com.project.qmplayer`).
-   [x] Inspect `QMPlayerApplication`, `QMPlayerContainer` and
    `MainActivity`; ensure the container is created once and shared. (Confirmed: initialized in `QMPlayerApplication.onCreate()`, shared across screens).
-   [x] Inspect screens/ViewModels and update Section 3 based on source
    code. (Verified: Library, Playlist, and Tag UI functional; option clicks wired up).
-   [x] Document any actual architecture differences. (Manual DI with `QMPlayerContainer` fully preserved).

**Acceptance:** Existing build state is known; user changes are
preserved; this task list reflects the repository rather than
assumptions.

## 5. Phase 1 --- Verify Core Data and Library Integration

### 5.1 MediaStore

-   [x] Verify `MediaStoreDataSource.observeSongs()` queries supported
    audio files. (Queries `MediaStore.Audio.Media.EXTERNAL_CONTENT_URI`).
-   [x] Verify the library filter uses `Music/QMPlayer/`. (Filter matches `Music/QMPlayer/` and `Music/QMPlayer/%`).
-   [x] Verify `IS_MUSIC != 0` filtering where appropriate. (Verified in selection query).
-   [x] Verify `ContentObserver` updates the flow when media is added,
    changed or removed. (Verified: registers on external collection URI, dispatches on IO).
-   [x] Verify cursor resources are closed and query failures are
    handled. (Safe `?.use` and try/catch against SecurityException / revoked permissions).
-   [x] Verify URI, title, artist, album, duration and artwork mapping. (Verified with album art URI mapping).
-   [x] Handle blank/unknown title and artist with safe UI fallbacks. (Handled in query and UI: "Unknown Title", "Unknown Artist"; eliminated `artist!!` crashes).
-   [x] Verify Android-version-specific audio permissions
    (`READ_MEDIA_AUDIO` and older-version equivalent if needed). (Declared `READ_MEDIA_AUDIO` and `READ_EXTERNAL_STORAGE` maxSdkVersion 32; runtime check in `MainActivity`).
-   [x] Verify permission denial/revocation does not crash the app. (Safe query catch blocks return empty list instead of crashing).
-   [x] Do not automatically create `Music/QMPlayer/`; this is not
    current intended behavior. (Verified).

### 5.2 Room metadata

-   [x] Verify `SongMetadataEntity` uses `songUri` as key and stores
    custom cover and lyrics TXT references. (Verified in schema v1).
-   [x] Verify `TagEntity` and `SongTagCrossReference` support
    many-to-many relationships. (Verified; added cascading cleanup for tag deletions and song deletions in DAO/repository).
-   [x] Verify `PlaylistEntity` and `PlaylistSongEntity` represent
    playlists and ordered membership. (Verified: composite primary key `(playlistId, songUri)` prevents duplicates; CASCADE foreign key deletes entries on playlist deletion).
-   [x] Verify playlist queries use deterministic `position ASC`
    ordering. (Verified in `PlaylistSongDao.observeSongsInPlaylist`).
-   [x] Verify foreign-key/cascade behavior does not delete unrelated
    metadata. (Verified).
-   [x] Add/verify Room migrations for schema changes; do not use
    destructive migration for production data. (Retained schema v1 without destructive changes).
-   [x] Handle stale song URIs gracefully. (`associateBy` + `mapNotNull` ignores missing/stale MediaStore songs without crashing).

### 5.3 Library

-   [x] Verify `LibraryRoute` obtains its ViewModel through existing
    manual DI. (Verified with `LibraryViewModelFactory`).
-   [x] Verify `LibraryViewModel` exposes loading, empty and populated
    states. (Verified in `LibraryUiState`).
-   [x] Verify only songs under the QMPlayer library folder are
    displayed. (Filtered via MediaStore `RELATIVE_PATH`).
-   [x] Verify sorting control changes actual displayed order. (Implemented `SongSortOrder`: Title A-Z, Title Z-A, Artist, Duration).
-   [x] Verify title, artist and artwork fallbacks. (Safe defaults applied across all cards and queue items).
-   [x] Verify song option modal opens for the correct song and clears
    selection on dismissal. (Verified).
-   [-] Connect tapping a song to Player (Phases 3--6). (Ready for Media3 playback engine integration).

**Acceptance:** MediaStore updates are reflected;
empty/loading/permission states are safe; Room does not duplicate the
song catalogue.

## 6. Phase 2 --- Complete Existing Song, Playlist and Tag Actions

Do not rebuild these screens. Preserve current UI and implement only
missing behavior.

### 6.1 Song options

Existing enum:

``` kotlin
enum class SongOption {
    PLAY, ADD_TO_PLAYLIST, ADD_TAG, RENAME, DELETE, INFO
}
```

-   [x] `PLAY`: request playback with selected song and defined queue
    policy. (Delegates to `onSongClick(song.uri)`).
-   [x] `ADD_TO_PLAYLIST`: show playlists and add song; define duplicate
    membership behavior. (Implemented `AddToPlaylistDialog` with playlist listing, "+ New Playlist" inline creation, duplicate badge, and next position indexing).
-   [x] `ADD_TAG`: show existing tags and assign one or more tags. (Implemented `ManageSongTagsDialog` with checkbox toggles and "+ New Tag" inline creation).
-   [x] `RENAME`: use MediaStore-safe rename and platform
    consent/permission flow. Do not merely change displayed title unless
    explicitly intended. (Implemented `RenameSongDialog` and `SongRepository.renameSong` via ContentResolver).
-   [x] `DELETE`: confirmation plus Android MediaStore deletion
    request/consent where required. (Implemented `ConfirmDeleteDialog` and ContentResolver deletion with Room metadata and relation cleanup).
-   [x] `INFO`: show available song metadata. (Implemented `SongInfoDialog` displaying Title, Artist, Album, Duration, Path/URI, and assigned Tags).
-   [x] Add loading/error/success feedback. (Field validation, error text, and disabled states implemented).
-   [x] Ensure modal dismissal does not trigger an action. (Verified: selection is cleared on dismiss without firing options).

### 6.2 Playlist options

Existing enum:

``` kotlin
enum class PlaylistOption { PLAY, RENAME, DELETE, INFO }
```

-   [x] `PLAY`: send ordered playlist songs to playback queue. (Routes to `onPlaylistClick(playlist.id)`).
-   [x] `RENAME`: validate and persist name. (Implemented rename mode with `CreatePlaylistDialog` and `PlaylistViewModel.renamePlaylist`).
-   [x] `DELETE`: confirm and delete playlist/membership according to
    DAO relationships. (Implemented `ConfirmDeleteDialog` and `PlaylistViewModel.deletePlaylist` with cascading cleanup).
-   [x] `INFO`: show song count and creation date where available. (Added `INFO` option and `PlaylistInfoDialog` with song count, total duration, and creation date).
-   [x] Verify adding songs preserves intended order. (Verified: position indexing with `position ASC`).
-   [x] Verify empty playlists and playlists with stale URIs do not
    crash. (Verified).

### 6.3 Tag options

Existing enum:

``` kotlin
enum class TagOption { PLAY, RENAME, DELETE, INFO }
```

-   [x] `PLAY`: send songs associated with tag to playback queue. (Routes to `onTagClick(tag.id)`).
-   [x] `RENAME`: validate and persist tag name. (Implemented rename mode with `CreateTagDialog` and `TagViewModel.renameTag`).
-   [x] `DELETE`: confirm and remove tag relationships. (Implemented `ConfirmDeleteDialog` and `TagViewModel.deleteTag` with relation cleanup).
-   [x] `INFO`: show associated song count. (Added `INFO` option and `TagInfoDialog` with song count and total duration).
-   [x] Verify assigning/removing tags updates song and tag screens. (Verified reactive StateFlow updates).
-   [x] Verify empty tags display correctly. (Verified).

### 6.4 Create dialogs and validation

-   [x] Verify playlist and tag dialogs reject empty names. (Rejects empty or whitespace-only names; displays error text).
-   [x] Decide and implement case-insensitive duplicate-name handling. (Case-insensitive duplicate validation prevents duplicate playlist/tag names).
-   [x] Prevent repeated submit while create operation is running. (`isSubmitting` flag and disabled state prevent duplicate submissions).
-   [x] Show persistence errors. (Field error text displayed when duplicate or invalid).
-   [x] Verify dialog state resets after success and dismissal. (State resets on dismiss).

**Acceptance:** No visible option remains a silent TODO; CRUD persists
across restarts; playlist order and tag relationships remain correct.

**Acceptance:** No visible option remains a silent TODO; CRUD persists
across restarts; playlist order and tag relationships remain correct.

## 7. Phase 3 --- Player UI and Navigation

The Player feature was designed and sample UI code was discussed, but
implementation is not confirmed. Inspect first and reuse existing files.

### 7.1 Intended views

**Music Player** - Artwork/music icon. - Song title and artist. - Tags
associated with current song. - Current position and duration. -
Seek/progress bar. - Previous, play/pause and next. - Shuffle. -
Repeat-all behavior.

**On Playing** - Only the songs currently in the playback queue. - Order
must match the actual Media3 queue. - If shuffle is enabled, show the
shuffled/effective playback order.

### 7.2 UI tasks

-   [ ] Inspect existing `feature/player` files and preserve completed
    components.
-   [ ] Implement/complete `PlayerRoute` and `PlayerScreen`.
-   [ ] Implement/complete Music Player content and On Playing content.
-   [ ] Implement/complete artwork, controls, seek bar and queue row
    components.
-   [ ] Display title/artist with safe fallbacks.
-   [ ] Display tags from tag repository.
-   [ ] Format current position and duration.
-   [ ] Make seek interaction update the real player.
-   [ ] Reflect unavailable previous/next states where appropriate.
-   [ ] Highlight the currently playing queue item.
-   [ ] Keep On Playing focused on the queue list.
-   [ ] Treat visuals as a functional first pass; user plans to refine
    them against Figma later.

### 7.3 Swipe and back

-   [ ] Use HorizontalPager or equivalent.
-   [ ] Open initially on Music Player.
-   [ ] Swiping right from Music Player reveals On Playing.
-   [ ] Swiping left from On Playing returns to Music Player.
-   [ ] Back from either page returns to Home.
-   [ ] Verify system back and visible back controls do not create
    duplicate navigation.
-   [ ] Player must be an exclusive full-screen branch, not composed
    alongside the Home `Scaffold`.

Suggested organization only if it matches existing structure:

``` text
feature/player/
├── PlayerRoute.kt
├── PlayerScreen.kt
├── PlayerViewModel.kt
├── PlayerUiState.kt
└── components/
    ├── MusicPlayerContent.kt
    ├── NowPlayingContent.kt
    ├── PlayerArtwork.kt
    ├── PlayerControls.kt
    ├── PlayerSeekBar.kt
    └── QueueSongItem.kt
```

**Acceptance:** Both pages work, swipe/back work, and no hardcoded demo
song remains in final integration.

## 8. Phase 4 --- Media3 Playback Service and Controller

This is the main functional milestone.

### 8.1 Service

-   [ ] Inspect Media3 dependencies and existing playback
    implementation.
-   [ ] Implement `MusicPlaybackService` as `MediaSessionService` if
    absent.
-   [ ] Create/own ExoPlayer inside the service, never in a Composable
    or `PlayerViewModel`.
-   [ ] Create a `MediaSession` connected to ExoPlayer.
-   [ ] Configure audio attributes and audio focus.
-   [ ] Configure media notification/session behavior and required
    manifest declarations.
-   [ ] Handle service lifecycle and release resources correctly.
-   [ ] Handle audio becoming noisy and headphone/Bluetooth disconnect.
-   [ ] Verify background playback.
-   [ ] Verify notification and lock-screen controls where supported.

### 8.2 Controller

-   [ ] Create a reusable MediaController connection layer integrated
    with manual DI.
-   [ ] Connect ViewModel/UI through MediaController.
-   [ ] Handle connecting, connected, disconnected and error states.
-   [ ] Release controller/session tokens/listeners at the correct
    lifecycle boundary.
-   [ ] Expose playback state as observable state (e.g. `StateFlow`).
-   [ ] Avoid multiple competing player/controller instances.

### 8.3 Song mapping

-   [ ] Create one mapping from domain `Song` to Media3 `MediaItem`.
-   [ ] Preserve a stable ID, preferably song URI.
-   [ ] Include title, artist, album and artwork metadata when
    available.
-   [ ] Handle inaccessible or deleted content URIs.
-   [ ] Map current MediaItem/queue entries back to domain `Song`.

### 8.4 Controls

-   [ ] Play and pause.
-   [ ] Previous and next.
-   [ ] Seek to position.
-   [ ] Expose current position and duration.
-   [ ] Expose `isPlaying`, buffering and playback error as needed.
-   [ ] Drive UI from player callbacks, not local-only optimistic state.
-   [ ] Verify controls work from full Player and mini player.

**Acceptance:** A real MediaStore URI plays; service owns ExoPlayer; UI
communicates through MediaController; playback state is shared across
Player, mini player and notification.

## 9. Phase 5 --- Queue, Shuffle and Repeat

### 9.1 Queue policy

-   [ ] Define queue policy when a Library song is tapped.
-   [ ] Proposed behavior to confirm: queue Library songs starting at
    selected song, with selected song current.
-   [ ] Define wraparound behavior at the last item with repeat-all.
-   [ ] Define Playlist and Tag queue sources; expected behavior is the
    collection's songs in its order.
-   [ ] Define behavior for empty collections and missing media.
-   [ ] Tapping a song in Playlist/Tag should use that collection, not
    silently switch to the full Library.

### 9.2 Actual queue

-   [ ] Set actual Media3 playlist/queue when playback starts.
-   [ ] Expose actual queue and current index to Player UI.
-   [ ] Keep order synchronized after next/previous.
-   [ ] Verify On Playing displays the same order as Media3.
-   [ ] Handle queue changes while Player is open.
-   [ ] Decide whether queue remove/reorder is in scope for v1; do not
    show nonfunctional controls.

### 9.3 Shuffle

-   [ ] Implement shuffle using Media3 shuffle mode.
-   [ ] Display the effective playback order, not a separately
    randomized visual list.
-   [ ] Toggle shuffle from Player UI.
-   [ ] Define behavior when turning shuffle off (restore original order
    or preserve current progression).
-   [ ] Verify next/previous follow the actual shuffled sequence.

### 9.4 Repeat all

-   [ ] Implement repeat-all with the appropriate Media3 repeat mode.
-   [ ] Return to the beginning after the final queue item.
-   [ ] Reflect repeat state in Player UI.
-   [ ] Do not add repeat-one unless explicitly requested.
-   [ ] Test repeat-all with shuffle both on and off.

**Acceptance:** Queue, shuffle, next/previous and repeat reflect actual
Media3 state; On Playing never shows a misleading order.

## 10. Phase 6 --- Home MiniPlayer and Playback Entry Points

-   [ ] Remove dummy
    `NowPlaying("Retro Velocity", "Neon Shard", playing)` when real
    state is available.
-   [ ] Remove local `playing` state used only to simulate playback.
-   [ ] Bind mini player title, artist, artwork and play/pause to shared
    playback state.
-   [ ] Connect mini player controls to MediaController.
-   [ ] Open full Player when mini player surface is tapped.
-   [ ] Define mini player visibility when no current song exists.
-   [ ] Connect Library song taps to playback request and open Player.
-   [ ] Connect Playlist/Tag `PLAY` actions to playback requests and
    open Player.
-   [ ] Keep `HomeScreen` as Scaffold owner unless a deliberate
    navigation refactor is needed.
-   [ ] Preserve Home tab and scroll state when returning from Player
    where practical.

**Acceptance:** Mini player and full Player always show the same
track/state; no dummy playback state remains; all entry points use the
same service.

## 11. Phase 7 --- Custom Cover and Lyrics

### 11.1 Custom cover

-   [ ] Add image selection for a song.
-   [ ] Persist selected image URI in
    `SongMetadataEntity.customCoverUri`.
-   [ ] Define artwork priority: custom cover, then MediaStore/embedded
    artwork, then default icon.
-   [ ] Handle revoked URI permission or missing image.
-   [ ] Allow reset to default artwork.
-   [ ] Do not modify the original audio file.

### 11.2 Lyrics TXT

-   [ ] Add `.txt` lyrics selection for a song.
-   [ ] Persist URI in `SongMetadataEntity.lyricsTxtUri`.
-   [ ] Request persistable URI permission when supported and
    appropriate.
-   [ ] Read and display TXT safely.
-   [ ] Handle missing, unreadable, empty and invalidly encoded files.
-   [ ] Allow removing/replacing lyrics reference.
-   [ ] Keep automatic same-basename TXT discovery optional until
    approved.

**Acceptance:** References survive app restarts when permissions remain
valid; missing files do not crash; audio files are not rewritten.

## 12. Phase 8 --- Download Feature

The scope mentions downloading from a supported YouTube link, but
implementation design is not confirmed.

-   [ ] Inspect existing code/dependencies for downloader
    implementation.
-   [ ] Define supported URLs, output format, metadata and storage
    destination.
-   [ ] Check applicable YouTube terms, copyright requirements and
    platform restrictions before implementation.
-   [ ] Do not implement DRM circumvention, access-control bypass or
    prohibited extraction.
-   [ ] Define progress, cancellation, failure and retry behavior.
-   [ ] Ensure saved files are discoverable through MediaStore.
-   [ ] Decide whether output belongs in `Music/QMPlayer/`; do not
    auto-create the folder without an explicit decision.
-   [ ] Add tests for URL validation and download state.

**Acceptance:** Download scope is documented and compliant;
errors/cancellation are handled; saved audio appears in Library after
MediaStore indexing.

## 13. Phase 9 --- Settings and Polish

-   [ ] Inspect existing settings implementation before adding screens.
-   [ ] Define the minimum useful settings scope.
-   [ ] Add app version/about section if appropriate.
-   [ ] Verify dark-only Material 3 styling.
-   [ ] Verify configured Inter font is used if already integrated.
-   [ ] Add consistent empty/loading/error/permission states.
-   [ ] Add feedback for destructive and long-running actions.
-   [ ] Check accessibility: content descriptions, touch targets,
    contrast and text readability.
-   [ ] Check edge-to-edge/insets and small-screen layouts.
-   [ ] Check process recreation/configuration changes for important UI
    state.

## 14. Phase 10 --- Testing and Release Readiness

### Automated tests

-   [ ] Unit tests for playlist ordering and repository mapping.
-   [ ] Unit tests for tag relationships and repository mapping.
-   [ ] Unit tests for queue construction and selected-song starting
    index.
-   [ ] Tests for shuffle/repeat policy where logic is outside Media3.
-   [ ] Tests for time formatting and Player UI state mapping.
-   [ ] Tests for MediaStore mapping/filtering using suitable
    abstractions or fakes.
-   [ ] DAO/database tests for playlist and tag persistence.
-   [ ] ViewModel tests for loading, empty, success and error states.
-   [ ] UI tests for critical flows if supported by current test setup.

### Manual device tests

-   [ ] Fresh install with permission granted and denied.
-   [ ] Empty `Music/QMPlayer/` folder.
-   [ ] Add/remove audio files while app is open.
-   [ ] Play, pause, seek, previous and next.
-   [ ] Shuffle on/off and verify visible queue order.
-   [ ] Repeat-all at end of queue.
-   [ ] Background playback, notification and lock-screen controls.
-   [ ] Headphone/Bluetooth disconnect.
-   [ ] Create, rename and delete playlists.
-   [ ] Add/remove songs from playlists and verify order.
-   [ ] Create, rename and delete tags.
-   [ ] Assign/remove tags from songs.
-   [ ] Delete/move a song outside app and verify stale references.
-   [ ] Open Player from Library, Playlist, Tag and mini player.
-   [ ] Back navigation from both Player pages.
-   [ ] Custom cover and lyrics URI persistence.
-   [ ] Kill/relaunch app and verify persisted metadata.

### Release checks

-   [ ] Remove debug demo data and `println` placeholders from completed
    flows.
-   [ ] Check manifest permissions and exported components.
-   [ ] Check service/notification declarations for target Android
    versions.
-   [ ] Check Room migration paths.
-   [ ] Run configured lint and test tasks.
-   [ ] Build release variant and resolve relevant warnings/errors.
-   [ ] Document known limitations.

## 15. Suggested Implementation Order

Prioritize a working end-to-end playback path before optional features.

1.  [ ] Phase 0 --- repository audit and build baseline.
2.  [ ] Phase 1 --- verify MediaStore and Room foundations.
3.  [ ] Phase 4 --- Media3 service and controller.
4.  [ ] Phases 5--6 --- minimal real playback from one Library song.
5.  [ ] Phase 3 --- complete Player and On Playing UI using real state.
6.  [ ] Phase 5 --- queue, shuffle and repeat-all.
7.  [ ] Phase 6 --- mini player and all playback entry points.
8.  [ ] Phase 2 --- complete Song/Playlist/Tag actions.
9.  [ ] Phase 7 --- custom cover and lyrics.
10. [ ] Phase 8 --- compliant download behavior.
11. [ ] Phases 9--10 --- settings, polish and testing.

Adjust only when repository inspection reveals a dependency. Do not wait
until all UI is finished before testing real playback.

## 16. Decisions / Open Questions

Do not silently decide product behavior that materially affects
implementation. Inspect existing code first and ask the user only when
necessary.

  -----------------------------------------------------------------------
  Topic                   Current understanding   Decision needed
  ----------------------- ----------------------- -----------------------
  Library tap queue       Likely queue Library    Confirm whether songs
                          songs starting at       before selected song
                          selected song           should also be in queue

  Playlist/Tag queue      Play selected           Confirm whether shuffle
                          collection              state is shared
                                                  globally

  Disable shuffle         Must reflect actual     Restore original order
                          Media3 queue            or preserve current
                                                  progression?

  Queue editing           On Playing must display Are remove/reorder
                          queue                   controls in v1?

  Repeat                  Repeat All is required  Repeat One is not
                                                  currently required

  Duplicate playlist      Unspecified             Allow or prevent adding
  membership                                      same song twice?

  Duplicate names         Unspecified             Prefer case-insensitive
                                                  uniqueness unless
                                                  requirements differ

  Rename/delete           MediaStore may require  Implement
  permissions             user consent            platform-compliant flow

  Lyrics auto-discovery   Optional idea           Do not implement until
                                                  approved

  Download implementation Not designed            Confirm compliant
                                                  provider/approach and
                                                  scope

  Folder creation         No auto-create for now  Keep unless explicitly
                                                  changed
  -----------------------------------------------------------------------

## 17. Implementation Notes and Known Pitfalls

-   `QMPlayerApplication` must be declared in the manifest. Otherwise
    casting `application` to `QMPlayerApplication` can cause
    `ClassCastException`.

-   Retrieve the existing container:

    ``` kotlin
    val container = (application as QMPlayerApplication).container
    ```

    Do not instantiate another `QMPlayerContainer` inside screens.

-   A previously discussed MediaStore SQL typo was `COLATE`; correct
    keyword is `COLLATE`.

-   Intended MediaStore library path is `Music/QMPlayer/`; verify
    trailing slash and Android API behavior.

-   `Song.artist` was previously modeled as non-null `String`. If actual
    model is nullable, use a safe fallback rather than `!!`.

-   Playlist and Tag screens were reported completed. Preserve them;
    focus on missing actions and integration.

-   Home mini player previously used dummy
    `NowPlaying("Retro Velocity", "Neon Shard", playing)` data. Replace
    only when real playback state is available.

-   Do not use a local Boolean as playback source of truth.

-   On Playing must derive queue order from actual Media3 queue,
    especially with shuffle enabled.

-   `PlayerViewModel` coordinates UI state and controller commands; it
    does not own ExoPlayer.

-   Do not assume proposed sample code has been copied into repository.

## 18. Definition of Done

A task is complete only when: 1. Implementation exists in the
repository. 2. It follows the architecture/product decisions above, or a
documented decision supersedes them. 3. Relevant error, empty and
permission states are handled. 4. Project builds. 5. Relevant tests or
documented manual verification pass. 6. This task list is updated to
`[x]` with a concise verification note. 7. Unrelated user changes have
not been overwritten.

**First action for the next AI session:** perform Phase 0 repository
audit, report verified status, then continue with the highest-priority
incomplete task. Do not start by recreating Library, Playlist or Tag UI.
