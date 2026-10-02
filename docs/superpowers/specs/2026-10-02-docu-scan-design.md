# DocuScan Design Specification

## Goal
Build a production-quality Android document scanner that captures paper documents with the camera, detects and corrects document geometry, enhances pages, creates multi-page PDFs locally, and provides a private on-device document library.

## Product principles
- Offline-first: scanning, image processing, PDF generation, and library operations work without an account or server.
- Fast capture: the camera experience should minimize manual alignment and taps.
- Recoverable editing: every captured page can be retaken, cropped, rotated, reordered, filtered, or deleted before PDF creation.
- Non-destructive processing: retain the original capture while generating the processed representation.
- Privacy by default: documents never leave the device in V1.

## V1 scope
### Capture
- CameraX preview.
- Rear camera as default.
- Torch control when supported.
- Manual capture.
- Automatic document detection and capture readiness indication.

### Geometry
- Detect four document corners from camera frames/captured images.
- Perspective correction using the selected quadrilateral.
- Manual four-corner adjustment.
- Rotation and crop adjustment.

### Image processing
- Original.
- Auto document enhancement.
- Grayscale.
- Black and white.
- Preserve sufficient resolution for readable text while controlling output size.

### Multi-page workflow
- Create a scan session.
- Add pages.
- Retake a page.
- Reorder pages.
- Delete pages.
- Preview all pages before export.

### PDF
- Generate multi-page PDF fully on-device.
- Preserve page orientation and order.
- Configurable output quality: small, medium, high.
- User-defined document name.
- Save locally and expose Android share/open actions.

### Library
- Recent scans list.
- Search by document name.
- Rename.
- Open/preview PDF.
- Share PDF.
- Delete scan and associated files.

## Explicitly out of V1
- Cloud sync.
- User accounts.
- Server-side processing.
- OCR/searchable PDFs.
- AI document classification.
- WhatsApp-specific integration.
- Life Admin integration.
- Subscription/payment system.

## Technical architecture
- Kotlin.
- Jetpack Compose for UI.
- CameraX for camera lifecycle and image capture.
- A dedicated document-detection abstraction so the detection implementation can evolve without coupling UI to CV logic.
- Dedicated perspective/geometry module.
- Dedicated image-processing module.
- Dedicated PDF generation module.
- Room for scan metadata.
- App-private filesystem for source and processed page images/PDFs.
- Android Sharesheet for export.

## Package boundaries
`camera`: CameraX lifecycle, preview, capture.
`detection`: document boundary/corner detection interfaces and implementation.
`geometry`: quadrilateral validation, perspective transform, rotation/crop.
`image`: filters and enhancement pipeline.
`scan`: scan-session/page domain models and editing operations.
`pdf`: PDF rendering and compression.
`data`: Room entities, DAOs, repositories, file storage.
`library`: library screens and document operations.
`ui`: shared Compose components, navigation, theme.

## Quality requirements
- Camera permission denial must produce a useful recovery path.
- Detection must tolerate modest perspective distortion, uneven lighting, and non-document background.
- Auto-capture must not repeatedly fire while the phone is moving.
- Manual capture must always remain available when detection fails.
- Processing failures must not destroy the source capture.
- Large multi-page scans must not require loading every full-resolution bitmap into memory simultaneously.
- Deleted scans must remove their associated files.
- Rotation/orientation must remain correct in both preview and exported PDF.

## Success criteria
A user can install DocuScan, grant camera permission, scan a physical multi-page document, correct any imperfect corners, apply a document filter, export a readable PDF, find it in Recent Scans, reopen it, and share it — entirely offline.
