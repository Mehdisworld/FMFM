package com.io.filemanager.freefileexplorer.easily.edit.imagezoom.paint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.io.filemanager.freefileexplorer.easily.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EraserConfigDialog extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {
    private Properties mProperties;

    public interface Properties {
        void onBrushSizeChanged(int i);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public Properties getmProperties() {
        return this.mProperties;
    }

    public void setmProperties(Properties properties) {
        this.mProperties = properties;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_eraser_config, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ((SeekBar) view.findViewById(R.id.sbSize)).setOnSeekBarChangeListener(this);
    }

    public void setPropertiesChangeListener(Properties properties) {
        this.mProperties = properties;
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        Properties properties;
        if (seekBar.getId() == R.id.sbSize && (properties = this.mProperties) != null) {
            properties.onBrushSizeChanged(i);
        }
    }
}
