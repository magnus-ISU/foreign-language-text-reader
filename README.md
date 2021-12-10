# foreign-language-text-reader
The Foreign Language Text Reader by lang-learn-guy on sourceforge, where he toils madly without version controlling I guess

## Links

Main website: https://sourceforge.net/projects/foreign-language-text-reader/

Download original tarball: https://sourceforge.net/projects/foreign-language-text-reader/files/

Online Documentation: https://foreign-language-text-reader.sourceforge.io

## File structure

- bin/       - Contains a prebuilt jarfile, the documentation PDF, and some demo data
- resources/ - Contains non-source files necessary to build or install FLTR
- src/       - Contains the source code of FLTR
- aur/       - For arch linux
- makefile   - Builds FLTR anywhere (anywhere civilized - idk how Windows works)
- README.md  - Documentation

## Building

Run `make`

## Installing

IMPORTANT! Only do this on Linux. Only tested on Arch, otherwise you might have to modify it

Run `sudo make install`

Installs to `/usr/local/` by default. Otherwise set the INSTALL\_PREFIX variable (and remember to run `sudo` with the `-E` option)

## LICENSE

Foreign Language Text Reader (FLTR) - A Tool for Language Learning.

Copyright © 2012-2021 FLTR Developers et al.

Permission is hereby granted, free of charge, to any person obtaining a
copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
________________________________________________________________________
