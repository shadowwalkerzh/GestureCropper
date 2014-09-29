package com.wan.cropper.cropwindow.handle;

import android.graphics.Rect;

import com.wan.cropper.cropwindow.edge.Edge;
import com.wan.cropper.util.AspectRatioUtil;

/**
 * HandleHelper class to handle vertical handles (i.e. left and right handles).
 */
class VerticalHandleHelper extends HandleHelper {

    private Edge mEdge;

    VerticalHandleHelper(Edge edge) {
        super(null, edge);
        mEdge = edge;
    }

    @Override
    void updateCropWindow(float x,
                          float y,
                          float targetAspectRatio,
                          Rect imageRect,
                          float snapRadius) {

        // Adjust this Edge accordingly.
        mEdge.adjustCoordinate(x, y, imageRect, snapRadius, targetAspectRatio);

        float left = Edge.LEFT.getCoordinate();
        float top = Edge.TOP.getCoordinate();
        float right = Edge.RIGHT.getCoordinate();
        float bottom = Edge.BOTTOM.getCoordinate();

        // After this Edge is moved, our crop window is now out of proportion.
        final float targetHeight = AspectRatioUtil.calculateHeight(left, right, targetAspectRatio);
        final float currentHeight = bottom - top;

        // Adjust the crop window so that it maintains the given aspect ratio by
        // moving the adjacent edges symmetrically in or out.
        final float difference = targetHeight - currentHeight;
        final float halfDifference = difference / 2;
        top -= halfDifference;
        bottom += halfDifference;

        Edge.TOP.setCoordinate(top);
        Edge.BOTTOM.setCoordinate(bottom);

        // Check if we have gone out of bounds on the top or bottom, and fix.
        if (Edge.TOP.isOutsideMargin(imageRect, snapRadius) && !mEdge.isNewRectangleOutOfBounds(Edge.TOP,
                                                                                                imageRect,
                                                                                                targetAspectRatio)) {
            final float offset = Edge.TOP.snapToRect(imageRect);
            Edge.BOTTOM.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);
        }
        if (Edge.BOTTOM.isOutsideMargin(imageRect, snapRadius) && !mEdge.isNewRectangleOutOfBounds(Edge.BOTTOM,
                                                                                                   imageRect,
                                                                                                   targetAspectRatio)) {
            final float offset = Edge.BOTTOM.snapToRect(imageRect);
            Edge.TOP.offset(-offset);
            mEdge.adjustCoordinate(targetAspectRatio);
        }
    }
}
