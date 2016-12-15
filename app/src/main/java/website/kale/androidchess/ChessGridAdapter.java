package website.kale.androidchess;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by douglasw on 12/14/16.
 */

public class ChessGridAdapter extends ArrayAdapter<ChessPiece> {
    ChessPiece[] pieces;

    public ChessGridAdapter(Context context, int resource, List<ChessPiece> objects) {
        super(context, resource, objects);
    }

    
}
