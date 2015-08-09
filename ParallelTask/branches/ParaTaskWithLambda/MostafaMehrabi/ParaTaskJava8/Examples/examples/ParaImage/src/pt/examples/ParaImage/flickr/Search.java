package pt.examples.ParaImage.flickr;

import java.awt.Image;
import java.util.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.xml.sax.SAXException;

import pt.runtime.CurrentTask;
import pt.examples.ParaImage.SearchProjectPanel;

import pt.examples.ParaImage.ImageManipulation;

import com.aetrion.flickr.*;
import com.aetrion.flickr.photos.*;

public class Search {
	
	public final static String API_KEY = "98cac2ba2664223a4cf1bb5b97d39fab";

	private static Flickr flickr = new Flickr(API_KEY);
	private static PhotosInterface photoInterface = flickr.getPhotosInterface();
//	private static int picsPerPage = 2;
	
//	public static Photo getPhoto(String id) {
//		Photo p = null;
//		try {
//			p = photoInterface.getPhoto(id);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (FlickrException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		}
//		return p;
//	}
	
	//IO_TASK
	public static Image getSquareImageTask(Photo p) {
		return getSquareImage(p);
	}
	
	public static Image getSquareImage(Photo p) {
		Image image = null;
		try {
			image = photoInterface.getImage(p, Size.SQUARE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public static Image getThumbnailImage(Photo p) {
		Image image = null;
		try {
			image = photoInterface.getImage(p, Size.THUMB);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	//IO_TASK
	public static Image getMediumImageTask(Photo p) {
		return getMediumImage(p);
	}
	
	
	public static Image getMediumImage(Photo p) {
		Image image = null;
		try {
			image = photoInterface.getImage(p, Size.MEDIUM);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public static Image getSmallImage(Photo p) {
		Image image = null;
		try {
			image = photoInterface.getImage(p, Size.SMALL);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return image;
	}
	public static Image getLargeImage(Photo p) {
		Image image = null;
		try {
			image = photoInterface.getImage(p, Size.LARGE);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return image;
	}
	
    /*
    private static void updatePanel(SearchProjectPanel panel, PhotoWithImage pi) {
    	panel.addToDisplay(pi);
    }
  private static void updatePanel(TaskID<Image> id) {
        Image image = null;
        try {
            image = id.getReturnResult();
            Photo p = (Photo)id.getTaskInfo().getTaskArguments()[0];
            final PhotoWithImage pi = new PhotoWithImage(p, image);
            SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
		            updatePanel(panel, pi);
				}
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */
   // private static SearchProjectPanel panel = null;
	
    //IO_TASK 
	public static List<PhotoWithImage> searchTask(String str, int picsPerPage, int pageOffset) {
    	return search(str, picsPerPage, pageOffset);
    }
	
    public static List<PhotoWithImage> search(String str, int picsPerPage, int pageOffset) {
		try {
			SearchParameters sp = new SearchParameters();
			sp.setText(str);
			PhotoList pl = photoInterface.search(sp, picsPerPage, pageOffset);
			List<PhotoWithImage> list = new ArrayList<PhotoWithImage>();
			for (int i = 0; i < pl.size(); i++) {
				Photo p = (Photo) pl.get(i);
				Image image = Search.getSquareImage(p);
				PhotoWithImage pi = new PhotoWithImage(p, image);
				//panel.addToDisplay(pi);
                if (CurrentTask.insideTask()) {
                	if (CurrentTask.cancelRequested()) {
                		CurrentTask.setProgress(100);
                		CurrentTask.publishInterim(pi);
                		return list;
                	} else {
                		CurrentTask.setProgress((int) ((i + 1.0) / pl.size() * 100));
                		CurrentTask.publishInterim(pi);
                	}
                }
				list.add(pi);
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
