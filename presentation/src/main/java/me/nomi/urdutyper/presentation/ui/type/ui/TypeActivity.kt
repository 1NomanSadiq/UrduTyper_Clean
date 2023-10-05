package me.nomi.urdutyper.presentation.ui.type.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import java.util.Random;

import dagger.hilt.android.AndroidEntryPoint;
import me.nomi.urdutyper.R;
import me.nomi.urdutyper.presentation.utils.common.ImageMaker;
import me.nomi.urdutyper.presentation.utils.common.MultiTouchListener;
import me.nomi.urdutyper.presentation.utils.common.Notification;

@AndroidEntryPoint
public class TypeActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TR_BL;
    Typeface[] fonts;
    int fontNumber = 8;
    boolean isBold;
    boolean isItalic;
    int newTextColor;
    CheckBox bold, italic;
    int leftGradientColor = 0xFF3F51B5, rightGradientColor = 0xFF3F51B5;


    RelativeLayout root;
    EditText editText;
    Dialog fontStyle, backgroundStyle, leftGradientDialog, rightGradientDialog;
    com.getbase.floatingactionbutton.FloatingActionButton textStyle, backgroundColorStyle, saveImage;
    FloatingActionsMenu floatingActionsMenu;
    FloatingActionButton alignLeft, alignRight, alignCenter, textColor, autoTextColor, gradientOrientation, leftGradient, rightGradient, autoBackgroundColor, solidBackgroundColor, myFont;
    RadioButton gradient, solid;
    SeekBar fontSize;
    float size;
    Dialog textColorPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        newTextColor = Color.BLACK;
        textStyle = findViewById(R.id.textStyle2);
        backgroundColorStyle = findViewById(R.id.backgroundColor2);
        floatingActionsMenu = findViewById(R.id.menu2);
        saveImage = findViewById(R.id.saveImage2);
        editText = findViewById(R.id.edittext2);
        editText.setOnTouchListener(new MultiTouchListener(this));
        size = 50;
        editText.setTextSize(size);
        isBold = false;
        isItalic = false;
        fonts = new Typeface[]{
                ResourcesCompat.getFont(this, R.font.a_300_regular),
                ResourcesCompat.getFont(this, R.font.aa_sameer_almas_regular),
                ResourcesCompat.getFont(this, R.font.aa_sameer_divangiry_regular),
                ResourcesCompat.getFont(this, R.font.aa_sameer_qamri_regular),
                ResourcesCompat.getFont(this, R.font.aa_sameer_rafiya_unicode_regular),
                ResourcesCompat.getFont(this, R.font.aa_sameer_zikran_regular),
                ResourcesCompat.getFont(this, R.font.aadil_aadil),
                ResourcesCompat.getFont(this, R.font.gandhara_suls_regular),
                ResourcesCompat.getFont(this, R.font.jameel_noori_nastaleeq),
                ResourcesCompat.getFont(this, R.font.jameel_noori_nastaleeq_kasheeda),
                ResourcesCompat.getFont(this, R.font.lobster_regular),
                ResourcesCompat.getFont(this, R.font.alqalam_khat_e_sumbali_regular),
                ResourcesCompat.getFont(this, R.font.alqalam_makki_regular)

        };

        Random random = new Random();

        root = findViewById(R.id.root2);
        setGradientBackground();


        root.setOnClickListener(v -> {
            floatingActionsMenu.collapse();
            editText.clearFocus();
        });

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (editText.hasFocus())
                floatingActionsMenu.collapse();
        });

        editText.setOnClickListener(v -> floatingActionsMenu.collapse());
        editText.setOnEditorActionListener((v13, actionId, event) -> actionId == EditorInfo.IME_ACTION_DONE);
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (hasFocus) {
                imm.showSoftInputFromInputMethod(v.getWindowToken(), 0);
            } else
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        });

        saveImage.setOnClickListener(v1 -> {
            if (!checkPermission()) {
                floatingActionsMenu.collapse();
                Toast.makeText(this, "Please allow access to storage then try again", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            } else {
                floatingActionsMenu.collapse();
                floatingActionsMenu.setVisibility(View.GONE);
                editText.setCursorVisible(false);
                editText.clearFocus();
                Uri uri = saveAndUploadFileOnline();
                Notification.INSTANCE.create(this, ImageMaker.INSTANCE.getPictureFile().getName(), uri);
                floatingActionsMenu.setVisibility(View.VISIBLE);
            }
        });


        backgroundColorStyle.setOnClickListener(v -> {
            floatingActionsMenu.collapse();
            backgroundStyle = new Dialog(this, R.style.WideDialog);
            backgroundStyle.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            backgroundStyle.setContentView(R.layout.background_color_dialog);
            Window window = backgroundStyle.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(layoutParams);

            gradientOrientation = backgroundStyle.findViewById(R.id.orientation);
            leftGradient = backgroundStyle.findViewById(R.id.leftGradient);
            rightGradient = backgroundStyle.findViewById(R.id.rightGradient);
            solidBackgroundColor = backgroundStyle.findViewById(R.id.solidBackgroundColor);
            autoBackgroundColor = backgroundStyle.findViewById(R.id.autoBackgroundColor);

            solid = backgroundStyle.findViewById(R.id.solid);
            gradient = backgroundStyle.findViewById(R.id.gradient);


            gradientOrientation.setOnClickListener(v1 -> {
                if (orientation == GradientDrawable.Orientation.TR_BL)
                    orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                else if (orientation == GradientDrawable.Orientation.TOP_BOTTOM)
                    orientation = GradientDrawable.Orientation.TL_BR;
                else if (orientation == GradientDrawable.Orientation.TL_BR)
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                else
                    orientation = GradientDrawable.Orientation.TR_BL;
                setGradientBackground();
            });


            autoBackgroundColor.setOnClickListener(v1 -> {
                if (solid.isChecked()) {
                    leftGradientColor = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt());
                    rightGradientColor = leftGradientColor;
                } else {
                    leftGradientColor = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt());
                    rightGradientColor = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt());
                }
                setGradientBackground();
            });

            gradient.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    gradientOrientation.setVisibility(View.VISIBLE);
                    leftGradient.setVisibility(View.VISIBLE);
                    rightGradient.setVisibility(View.VISIBLE);
                    solidBackgroundColor.setVisibility(View.GONE);
                } else {
                    solidBackgroundColor.setVisibility(View.VISIBLE);
                    gradientOrientation.setVisibility(View.GONE);
                    leftGradient.setVisibility(View.GONE);
                    rightGradient.setVisibility(View.GONE);
                }
            });


            leftGradient.setOnClickListener(v1 -> {
                showLeftGradientBackgroundColorPickDialog();
                setGradientBackground();
            });

            rightGradient.setOnClickListener(v1 -> showRightGradientBackgroundColorPickDialog());

            solidBackgroundColor.setOnClickListener(v1 -> showSolidBackgroundColorPickDialog());
            backgroundStyle.show();
        });


        textStyle.setOnClickListener(v -> {
            floatingActionsMenu.collapse();
            fontStyle = new Dialog(this, R.style.WideDialog);
            fontStyle.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            fontStyle.setContentView(R.layout.font_style_dialog);
            Window window = fontStyle.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(layoutParams);

            alignLeft = fontStyle.findViewById(R.id.alignLeft);
            alignRight = fontStyle.findViewById(R.id.alignRight);
            alignCenter = fontStyle.findViewById(R.id.alignCenter);
            fontSize = fontStyle.findViewById(R.id.fontSize);
            textColor = fontStyle.findViewById(R.id.textColor);
            autoTextColor = fontStyle.findViewById(R.id.autoTextColor);
            bold = fontStyle.findViewById(R.id.bold);
            italic = fontStyle.findViewById(R.id.italic);
            myFont = fontStyle.findViewById(R.id.myFont);


            bold.setOnCheckedChangeListener(this);
            italic.setOnCheckedChangeListener(this);

            fontSize.setProgress((int) size - 20);

            alignLeft.setOnClickListener(v1 -> {
                editText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                editText.setTextSize(size + 1);
                editText.setTextSize(size - 1);
            });
            alignRight.setOnClickListener(v1 -> {
                editText.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
                editText.setTextSize(size + 1);
                editText.setTextSize(size - 1);
            });
            alignCenter.setOnClickListener(v1 -> {
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText.setTextSize(size + 1);
                editText.setTextSize(size - 1);
            });
            autoTextColor.setOnClickListener(v1 -> {
                int color = Color.rgb(random.nextInt(), random.nextInt(), random.nextInt());

                editText.setTextColor(color);
                editText.setHintTextColor(Color.argb(50, Color.red(color), Color.green(color), Color.blue(color)));
            });

            textColor.setOnClickListener(v1 -> showTextColorPickDialog());

            myFont.setOnClickListener(v12 -> {
                if (fontNumber == fonts.length - 1)
                    fontNumber = 0;
                else
                    fontNumber = fontNumber + 1;
                editText.setTypeface(fonts[fontNumber]);
            });

            fontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    size = progress + 20;
                    editText.setTextSize(size);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            fontStyle.show();

        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (italic.isChecked()) {
            if (!bold.isChecked()) {
                editText.setTypeface(editText.getTypeface(), Typeface.ITALIC);
            } else
                editText.setTypeface(editText.getTypeface(), Typeface.BOLD_ITALIC);
        } else {
            if (bold.isChecked()) {
                editText.setTypeface(editText.getTypeface(), Typeface.BOLD);
            } else {
                editText.setTypeface(Typeface.create(editText.getTypeface(), Typeface.NORMAL));
            }
        }
    }


    public void showTextColorPickDialog() {
        textColorPicker = new Dialog(this, R.style.WideDialog);
        textColorPicker.setContentView(R.layout.color_pick_dialog);
        Window window = textColorPicker.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(layoutParams);
        ColorPicker picker = textColorPicker.findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
        SVBar svBar = textColorPicker.findViewById(R.id.svbar);
        FloatingActionButton okButton = textColorPicker.findViewById(R.id.okButton);
        picker.addSVBar(svBar);
        picker.setNewCenterColor(editText.getTextColors().getDefaultColor());
        if (editText.getTextColors().getDefaultColor() != Color.BLACK)
            picker.setColor(editText.getTextColors().getDefaultColor());
        else
            picker.setColor(0xFF000001);
        okButton.setOnClickListener(v -> {
            editText.setTextColor(picker.getColor());
            editText.setHintTextColor(Color.argb(60, Color.red(picker.getColor()), Color.green(picker.getColor()), Color.blue(picker.getColor())));
            textColorPicker.dismiss();
        });
        textColorPicker.show();
    }


    public void showSolidBackgroundColorPickDialog() {
        backgroundStyle = new Dialog(this, R.style.WideDialog);
        backgroundStyle.setContentView(R.layout.color_pick_dialog);
        Window window = backgroundStyle.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(layoutParams);
        ColorPicker picker = backgroundStyle.findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
        SVBar svBar = backgroundStyle.findViewById(R.id.svbar);
        FloatingActionButton okButton = backgroundStyle.findViewById(R.id.okButton);
        picker.addSVBar(svBar);
        picker.setNewCenterColor(leftGradientColor);
        if (leftGradientColor != Color.BLACK)
            picker.setColor(leftGradientColor);
        else
            picker.setColor(0xFF000001);
        okButton.setOnClickListener(v -> {
            leftGradientColor = picker.getColor();
            rightGradientColor = picker.getColor();
            root.setBackgroundColor(leftGradientColor);
            backgroundStyle.dismiss();
        });
        backgroundStyle.show();
    }


    public void showLeftGradientBackgroundColorPickDialog() {
        leftGradientDialog = new Dialog(this, R.style.WideDialog);
        leftGradientDialog.setContentView(R.layout.color_pick_dialog);
        Window window = leftGradientDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(layoutParams);
        ColorPicker picker = leftGradientDialog.findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
        SVBar svBar = leftGradientDialog.findViewById(R.id.svbar);
        FloatingActionButton okButton = leftGradientDialog.findViewById(R.id.okButton);
        picker.addSVBar(svBar);
        picker.setNewCenterColor(leftGradientColor);
        if (leftGradientColor != Color.BLACK)
            picker.setColor(leftGradientColor);
        else
            picker.setColor(0xFF000001);
        okButton.setOnClickListener(v -> {
            leftGradientColor = picker.getColor();
            setGradientBackground();
            leftGradientDialog.dismiss();
        });
        leftGradientDialog.show();
    }


    public void showRightGradientBackgroundColorPickDialog() {
        rightGradientDialog = new Dialog(this, R.style.WideDialog);
        rightGradientDialog.setContentView(R.layout.color_pick_dialog);
        Window window = rightGradientDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(layoutParams);
        ColorPicker picker = rightGradientDialog.findViewById(R.id.picker);
        picker.setShowOldCenterColor(false);
        SVBar svBar = rightGradientDialog.findViewById(R.id.svbar);
        FloatingActionButton okButton = rightGradientDialog.findViewById(R.id.okButton);
        picker.addSVBar(svBar);
        picker.setNewCenterColor(rightGradientColor);
        if (rightGradientColor != Color.BLACK)
            picker.setColor(rightGradientColor);
        else
            picker.setColor(0xFF000001);
        okButton.setOnClickListener(v -> {
            rightGradientColor = picker.getColor();
            setGradientBackground();
            rightGradientDialog.dismiss();
        });
        rightGradientDialog.show();
    }

    public void setGradientBackground() {
        GradientDrawable gd = new GradientDrawable(
                orientation,
                new int[]{leftGradientColor, rightGradientColor});
        root.setBackground(gd);
    }

    public boolean checkPermission() {
        return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public Uri saveAndUploadFileOnline() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String filepath = uid + "/Images/" + ImageMaker.INSTANCE.getPictureFile().getName() + ".jpg";

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(uid).child("Images");

        Uri path = ImageMaker.INSTANCE.saveBitMap(this, root);
        StorageReference mountainImagesRef = storageReference.child(filepath);
        if (path != null) {
            mountainImagesRef.putFile(path).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(uri -> ref.child(ImageMaker.INSTANCE.getPictureFile().getName()).setValue(uri.toString()));
                    Snackbar.make(root, "Uploaded Successfully", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(root, "Uploaded Failed", Snackbar.LENGTH_LONG).show();
                }
            });
        }
        return path;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("leftGradientColor", leftGradientColor);
        savedInstanceState.putInt("rightGradientColor", rightGradientColor);
        savedInstanceState.putFloat("fontSize", size);
        savedInstanceState.putInt("fontColor", editText.getCurrentTextColor());
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        leftGradientColor = savedInstanceState.getInt("leftGradientColor");
        rightGradientColor = savedInstanceState.getInt("rightGradientColor");
        orientation = GradientDrawable.Orientation.TR_BL;
        size = savedInstanceState.getFloat("fontSize");
        root = findViewById(R.id.root2);
        editText = findViewById(R.id.edittext2);
        editText.setTextSize(size);
        editText.setTextColor(savedInstanceState.getInt("fontColor"));
        setGradientBackground();
    }

    @Override
    public void onBackPressed() {
        if (!editText.isFocused()) {
            super.onBackPressed();
        } else {
            editText.clearFocus();
        }
    }
}
