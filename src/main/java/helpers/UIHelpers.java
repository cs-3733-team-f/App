package helpers;

import com.google.cloud.storage.*;
import controllers.maps.AdminMapController;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;
import models.map.Map;

import javax.imageio.ImageIO;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UIHelpers {

    public static final double MIN_WIDTH = 400.0;
    public static final double MIN_PIXELS = 600.0;

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

    public static void setAdminNodeClickEvent(Map map, AnchorPane[] panes, Location loc, Circle c) {
//        c.setOnMouseDragged(evt -> {
//            try {
//                evt.consume();
//                if(!AdminMapController.isEnableAddNode() && !AdminMapController.isEnableEditEdge()) {
//                    c.setCenterX(evt.getX());
//
//                    loc.setxCord((int)MapDisplay.revScaleX(evt.getX()));
//                    loc.setyCord((int)MapDisplay.revScaleY(evt.getY()));
//                    LocationTable.updateLocation(loc);
//                    c.setCenterY(evt.getY());
//                }
//            } catch (Exception e) {
//                throw new UnsupportedOperationException(e);
//            }
////        });
//        c.setOnMouseClicked(evt -> {
//            try {
//                evt.consume();
//                AdminMapController.locationSelectEvent(loc);
////                if(!AdminMapController.isEnableEditEdge())
////                    ScreenController.popUp(Constants.Routes.EDIT_LOCATION, loc, map, panes, c);
//            } catch (Exception e) {
//                throw new UnsupportedOperationException(e);
//            }
//        });
    }

    public static Line generateLineFromEdge(Edge e) {
        Location start = e.getStart();
        Location end = e.getEnd();
        Point startPoint = generateLocationCoordinates(start);
        Point endPoint = generateLocationCoordinates(end);
        Line line = new Line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(MapDisplay.getEdgeWidth());
//        lstLines.put(edge.getEdgeID(), line);
        return line;
//        pane.getChildren().add(line);
    }

    public static Point generateLocationCoordinates(Location loc) {
        double x = MapDisplay.scaleX(loc.getxCord());
        double y = MapDisplay.scaleY(loc.getyCord());
        return new Point((int) x, (int) y);
    }

    public static Circle updateCircleForNodeType(Location loc) {
        Circle c = loc.getNodeCircle();
        if(loc.getNodeType() == Constants.NodeType.HALL) {
            c.setRadius(MapDisplay.getHallRadius());
            c.setFill(Color.GRAY);
        } else {
            c.setRadius(MapDisplay.getLocRadius());
            c.setFill(Color.WHITE);
        }
        return c;
    }
    public void deleteThis() {
//        Storage storage = StorageOptions.getDefaultInstance().getService();
//
//        BlobId blobId = BlobId.of(STORAGE_BUCKET, "blob_name");
//        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/png").build();
//        BufferedImage bImage = ImageIO.read(new File("sample.png"));
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        ImageIO.write(bImage, "png", bos );
//        byte [] data = bos.toByteArray();
//        Blob blob = storage.create(blobInfo, data);

//        Bitmap bmp =  BitmapFactory.decodeResource(getResources(),
//                R.drawable.chicken);//your image
//        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
//        bmp.recycle();
//        byte[] byteArray = bYtE.toByteArray();
//        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}
