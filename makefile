INSTALL_PREFIX?=/usr/local

.PHONY=install install-helpers

all: FLTR.jar

FLTR.jar:
	mkdir -p build/
	javac -cp resources/miglayout-4.0-swing.jar:src/fltrpackage/:./src src/FLTR.java -d build
	cp resources/*.htm resources/*.png build/fltrpackage
	cp resources/miglayout-4.0-swing.jar build/foreign-language-text-reader.jar
	sh -c 'cd build && jar ufe foreign-language-text-reader.jar FLTR FLTR.class FLTR\$$1.class fltrpackage/*'


install-helpers:
	sed 's|INSTALL_PREFIX|$(INSTALL_PREFIX)|g' resources/linux/foreign-language-text-reader.sh > build/foreign-language-text-reader.sh
	sed 's|INSTALL_PREFIX|$(INSTALL_PREFIX)|g' resources/linux/foreign-language-text-reader.desktop > build/foreign-language-text-reader.desktop

install: all install-helpers
	install -Dm755 build/foreign-language-text-reader.sh $(INSTALL_PREFIX)/bin/
	install -Dm644 build/FLTR.jar $(INSTALL_PREFIX)/lib/foreign-language-text-reader/foreign-language-text-reader.jar
	install -Dm644 build/foreign-language-text-reader.desktop $(INSTALL_PREFIX)/share/applications/
	install -Dm644 resources/icon256.png $(INSTALL_PREFIX)/share/pixmaps/foreign-language-text-reader.png
