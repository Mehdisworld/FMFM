package com.io.filemanager.freefileexplorer.easily.edit.imagezoom.paint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.io.filemanager.freefileexplorer.easily.R;
import com.io.filemanager.freefileexplorer.easily.edit.ColorPickerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BrushConfigDialog extends BottomSheetDialogFragment implements SeekBar.OnSeekBarChangeListener {
    private Properties mProperties;

    public interface Properties {
        void onBrushSizeChanged(int i);

        void onColorChanged(int i);

        void onOpacityChanged(int i);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_brush_config, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvColors);
        ((SeekBar) view.findViewById(R.id.sbOpacity)).setOnSeekBarChangeListener(this);
        ((SeekBar) view.findViewById(R.id.sbSize)).setOnSeekBarChangeListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        recyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            public final void onColorPickerClickListener(int i) {
                BrushConfigDialog.this.lambda$onViewCreated$0$BrushConfigDialog(i);
            }
        });
        recyclerView.setAdapter(colorPickerAdapter);
    }

    public void lambda$onViewCreated$0$BrushConfigDialog(int i) {
        if (this.mProperties != null) {
            dismiss();
            this.mProperties.onColorChanged(i);
        }
    }

    public void setPropertiesChangeListener(Properties properties) {
        this.mProperties = properties;
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        Properties properties;
        int id = seekBar.getId();
        if (id == R.id.sbOpacity) {
            Properties properties2 = this.mProperties;
            if (properties2 != null) {
                properties2.onOpacityChanged(i);
            }
        } else if (id == R.id.sbSize && (properties = this.mProperties) != null) {
            properties.onBrushSizeChanged(i);
        }
    }
}
