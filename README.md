# MacOS Setup

## Prerequisites

1. **Enable Rosetta 2** (required on Apple Silicon Macs to run x86_64 binaries):
   ```bash
   # Check if Rosetta is already installed
   /usr/bin/pgrep -q oahd && echo "Rosetta is installed" || echo "Rosetta is NOT installed"
   
   # Install Rosetta if not already installed
   softwareupdate --install-rosetta --agree-to-license
   ```

2. Install JDK 11 (x64 architecture even on arm chips) from [this](https://adoptium.net/download?link=https%3A%2F%2Fgithub.com%2Fadoptium%2Ftemurin11-binaries%2Freleases%2Fdownload%2Fjdk-11.0.29%252B7%2FOpenJDK11U-jdk_x64_mac_hotspot_11.0.29_7.pkg&vendor=Adoptium) link.

3. Copy your Tesseract 4.x (x86_64) installation to `~/tools/tesseract/4.1.3/` or download it from a trusted source.

## Tesseract Setup

**Note:** SikuliX requires Tesseract 4.x and runs in x86_64 (Rosetta) mode on Apple Silicon Macs because the OpenCV library it uses doesn't ship ARM architecture.

4. Install x86_64 Leptonica (required dependency for Tesseract):
```bash
arch -x86_64 /usr/local/bin/brew install leptonica
```

5. Create symlink for Tesseract library:
```bash
sudo ln -sf ~/tools/tesseract/4.1.3/lib/libtesseract.4.dylib /usr/local/lib/libtesseract.dylib
```

6. Create symlink structure for Leptonica:
```bash
sudo mkdir -p /usr/local/opt/leptonica/lib
sudo rm /usr/local/Cellar/leptonica/1.86.0/lib/liblept.5.dylib  # Remove any broken symlinks
sudo ln -sf /usr/local/Cellar/leptonica/1.86.0/lib/libleptonica.6.dylib /usr/local/Cellar/leptonica/1.86.0/lib/liblept.5.dylib
sudo ln -sf /usr/local/Cellar/leptonica/1.86.0/lib/liblept.5.dylib /usr/local/opt/leptonica/lib/liblept.5.dylib
```

7. Remove quarantine attributes and re-sign libraries to bypass macOS Gatekeeper:
```bash
# Remove quarantine from Tesseract
sudo xattr -d com.apple.quarantine ~/tools/tesseract/4.1.3/lib/libtesseract.4.dylib 2>/dev/null || true
sudo codesign --force --deep --sign - ~/tools/tesseract/4.1.3/lib/libtesseract.4.dylib
sudo codesign --force --deep --sign - /usr/local/lib/libtesseract.dylib

# Re-sign Leptonica
sudo codesign --force --deep --sign - /usr/local/Cellar/leptonica/1.86.0/lib/libleptonica.6.dylib
```

8. Run tests to verify setup:
```bash
./gradlew clean test
```