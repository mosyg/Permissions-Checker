package edu.uiuc.permissionschecker.infocheck;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceView;
import edu.uiuc.permissionschecker.infocheck.Result.Attempt;

public class CameraInfo extends Info {
	public static String NAME = "Camera";
	public String getName() {
		return NAME;
	}
	@Override
	public Result find(Context context, OutputLog out) {
		final Result result = new Result(NAME);
		try {
			final Boolean[] keeptrying = { true };
			final Camera camera = Camera.open();
			SurfaceView view = new SurfaceView(context);
			camera.setPreviewDisplay(view.getHolder());
			camera.setPreviewCallback(new PreviewCallback() {
				@Override
				public void onPreviewFrame(byte[] data, Camera camera) {
					result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", "PREVIEW: "+data.toString() +" length "+data.length));
					keeptrying[0] = false;					
				}
			});
			camera.startPreview();
			camera.takePicture(null, new PictureCallback() {
				@Override
				public void onPictureTaken(byte[] arg0, Camera arg1) {
					Log.d("CameraInfo", "Got picture!");
					result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", arg0.toString() +" length "+arg0.length));
					keeptrying[0] = false;
				}}, new PictureCallback() {
				@Override
				public void onPictureTaken(byte[] arg0, Camera arg1) {
					Log.d("CameraInfo", "Got picture!");
					result.addAttempt(new Attempt(Result.NORMAL, Result.SUCCESS, "", arg0.toString() +" length "+arg0.length));
					keeptrying[0] = false;
					camera.stopPreview();
					camera.release();
				}
			});
			
			long timestart = System.currentTimeMillis();
			while(keeptrying[0]) {
				if (System.currentTimeMillis() - timestart > 1 * 1000 /* seconds */) {
					keeptrying[0] = false;
					result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, "picture timed out", ""));
				}
				Log.d("CameraInfo", "Waiting a second...");
				Thread.sleep(1000);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			result.addAttempt(new Attempt(Result.NORMAL, Result.FAILURE, exceptionToString(e), ""));
		}
		
		
		
        return result;
	}

}
