package LuceneComponentes;

import java.io.File;
import java.io.FileFilter;

public class FiltroArchivos implements FileFilter{
	@Override
	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".txt");
	}
}
