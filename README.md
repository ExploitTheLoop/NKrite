# NKrite

Patch runtime process library (*.so) with offset & hex bytes.

This project is designed for those who want to build an injector app without any C++ knowledge. **NKrite** supports both mobile devices and 64-bit emulators, including all the latest versions, eliminating the need for C++ executables or any other cross-platform languages. It is compatible with multiple architectures, including x86, x64, ARMv7a, ARMv8a, and more.

## Build & Usage

- **Download this project:**
    ```bash
    git clone https://github.com/ExploitTheLoop/NKrite
    ```
- **Open and build the project.**
- **Install the app on your Android devices:**
    - If you have root, grant root permissions.
    - If non-root, use a virtual app.
- **Open the app that you want to inject.**
- **Open NKrite.**
- **Fill in all needed information (arguments for the `changemem()` method):**
    - **Package Name:** The package of your process.
    - **Lib Name:** Library (*.so) name.
    - **Offset:** `0x...`.
    - **Hex:** `0A010213`.

## Credits

- [LibInjector](https://github.com/jbro129/LibInjector) by [jbro129](https://github.com/jbro129)
- [libsu](https://github.com/topjohnwu/libsu) by [topjohnwu](https://github.com/topjohnwu)
- [KMrite](https://github.com/BryanGIG/KMrite) by [BryanGIG](https://github.com/BryanGIG)
