package ir.mahdi.mzip.rar;

import ir.mahdi.mzip.rar.exception.RarException;
import ir.mahdi.mzip.rar.impl.FileVolumeManager;
import ir.mahdi.mzip.rar.io.IReadOnlyAccess;
import ir.mahdi.mzip.rar.rarfile.AVHeader;
import ir.mahdi.mzip.rar.rarfile.BaseBlock;
import ir.mahdi.mzip.rar.rarfile.BlockHeader;
import ir.mahdi.mzip.rar.rarfile.CommentHeader;
import ir.mahdi.mzip.rar.rarfile.EAHeader;
import ir.mahdi.mzip.rar.rarfile.EndArcHeader;
import ir.mahdi.mzip.rar.rarfile.FileHeader;
import ir.mahdi.mzip.rar.rarfile.MacInfoHeader;
import ir.mahdi.mzip.rar.rarfile.MainHeader;
import ir.mahdi.mzip.rar.rarfile.MarkHeader;
import ir.mahdi.mzip.rar.rarfile.ProtectHeader;
import ir.mahdi.mzip.rar.rarfile.SignHeader;
import ir.mahdi.mzip.rar.rarfile.SubBlockHeader;
import ir.mahdi.mzip.rar.rarfile.SubBlockHeaderType;
import ir.mahdi.mzip.rar.rarfile.UnixOwnersHeader;
import ir.mahdi.mzip.rar.rarfile.UnrarHeadertype;
import ir.mahdi.mzip.rar.unpack.ComprDataIO;
import ir.mahdi.mzip.rar.unpack.Unpack;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Archive implements Closeable {
    private static Logger logger = Logger.getLogger(Archive.class.getName());
    private int currentHeaderIndex;
    private final ComprDataIO dataIO;
    private final List<BaseBlock> headers;
    private MarkHeader markHead;
    private MainHeader newMhd;
    private IReadOnlyAccess rof;
    private long totalPackedRead;
    private long totalPackedSize;
    private Unpack unpack;
    private final UnrarCallback unrarCallback;
    private Volume volume;
    private VolumeManager volumeManager;


    public Archive(VolumeManager volumeManager2) throws RarException, IOException {
        this(volumeManager2, (UnrarCallback) null);
        UnrarCallback unrarCallback2 = null;
    }

    public Archive(VolumeManager volumeManager2, UnrarCallback unrarCallback2) throws RarException, IOException {
        this.headers = new ArrayList();
        this.markHead = null;
        this.newMhd = null;
        this.totalPackedSize = 0;
        this.totalPackedRead = 0;
        this.volumeManager = volumeManager2;
        this.unrarCallback = unrarCallback2;
        setVolume(volumeManager2.nextArchive(this, (Volume) null));
        this.dataIO = new ComprDataIO(this);
    }

    public Archive(File file) throws RarException, IOException {
        this((VolumeManager) new FileVolumeManager(file), (UnrarCallback) null);
        UnrarCallback unrarCallback2 = null;
    }

    public Archive(File file, UnrarCallback unrarCallback2) throws RarException, IOException {
        this((VolumeManager) new FileVolumeManager(file), unrarCallback2);
    }

    private void setFile(IReadOnlyAccess iReadOnlyAccess, long j) throws IOException {
        this.totalPackedSize = 0;
        this.totalPackedRead = 0;
        close();
        this.rof = iReadOnlyAccess;
        try {
            readHeaders(j);
        } catch (Exception e) {
            logger.log(Level.WARNING, "exception in archive constructor maybe file is encrypted or currupt", e);
        }
        for (BaseBlock next : this.headers) {
            if (next.getHeaderType() == UnrarHeadertype.FileHeader) {
                this.totalPackedSize += ((FileHeader) next).getFullPackSize();
            }
        }
        UnrarCallback unrarCallback2 = this.unrarCallback;
        if (unrarCallback2 != null) {
            unrarCallback2.volumeProgressChanged(this.totalPackedRead, this.totalPackedSize);
        }
    }

    public void bytesReadRead(int i) {
        if (i > 0) {
            long j = this.totalPackedRead + ((long) i);
            this.totalPackedRead = j;
            UnrarCallback unrarCallback2 = this.unrarCallback;
            if (unrarCallback2 != null) {
                unrarCallback2.volumeProgressChanged(j, this.totalPackedSize);
            }
        }
    }

    public IReadOnlyAccess getRof() {
        return this.rof;
    }

    public List<FileHeader> getFileHeaders() {
        ArrayList arrayList = new ArrayList();
        for (BaseBlock next : this.headers) {
            if (next.getHeaderType().equals(UnrarHeadertype.FileHeader)) {
                arrayList.add((FileHeader) next);
            }
        }
        return arrayList;
    }

    public FileHeader nextFileHeader() {
        BaseBlock baseBlock;
        int size = this.headers.size();
        do {
            int i = this.currentHeaderIndex;
            if (i >= size) {
                return null;
            }
            List<BaseBlock> list = this.headers;
            this.currentHeaderIndex = i + 1;
            baseBlock = list.get(i);
        } while (baseBlock.getHeaderType() != UnrarHeadertype.FileHeader);
        return (FileHeader) baseBlock;
    }

    public UnrarCallback getUnrarCallback() {
        return this.unrarCallback;
    }

    public boolean isEncrypted() {
        MainHeader mainHeader = this.newMhd;
        Objects.requireNonNull(mainHeader, "mainheader is null");
        return mainHeader.isEncrypted();
    }

    private void readHeaders(long j) throws IOException, RarException {
        EndArcHeader endArcHeader;
        this.markHead = null;
        this.newMhd = null;
        this.headers.clear();
        int i = 0;
        this.currentHeaderIndex = 0;
        while (true) {
            int i2 = 7;
            byte[] bArr = new byte[7];
            long position = this.rof.getPosition();
            if (position < j && this.rof.readFully(bArr, 7) != 0) {
                BaseBlock baseBlock = new BaseBlock(bArr);
                baseBlock.setPositionInFile(position);
                switch (AnonymousClass2.$SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype[baseBlock.getHeaderType().ordinal()]) {
                    case 1:
                        MarkHeader markHeader = new MarkHeader(baseBlock);
                        this.markHead = markHeader;
                        if (markHeader.isSignature()) {
                            this.headers.add(this.markHead);
                            break;
                        } else {
                            throw new RarException(RarException.RarExceptionType.badRarArchive);
                        }
                    case 2:
                        if (!baseBlock.hasEncryptVersion()) {
                            i2 = 6;
                        }
                        byte[] bArr2 = new byte[i2];
                        this.rof.readFully(bArr2, i2);
                        MainHeader mainHeader = new MainHeader(baseBlock, bArr2);
                        this.headers.add(mainHeader);
                        this.newMhd = mainHeader;
                        if (!mainHeader.isEncrypted()) {
                            break;
                        } else {
                            throw new RarException(RarException.RarExceptionType.rarEncryptedException);
                        }
                    case 3:
                        byte[] bArr3 = new byte[8];
                        this.rof.readFully(bArr3, 8);
                        this.headers.add(new SignHeader(baseBlock, bArr3));
                        break;
                    case 4:
                        byte[] bArr4 = new byte[7];
                        this.rof.readFully(bArr4, 7);
                        this.headers.add(new AVHeader(baseBlock, bArr4));
                        break;
                    case 5:
                        byte[] bArr5 = new byte[6];
                        this.rof.readFully(bArr5, 6);
                        CommentHeader commentHeader = new CommentHeader(baseBlock, bArr5);
                        this.headers.add(commentHeader);
                        this.rof.setPosition(commentHeader.getPositionInFile() + ((long) commentHeader.getHeaderSize()));
                        break;
                    case 6:
                        if (baseBlock.hasArchiveDataCRC()) {
                            i = 4;
                        }
                        if (baseBlock.hasVolumeNumber()) {
                            i += 2;
                        }
                        if (i > 0) {
                            byte[] bArr6 = new byte[i];
                            this.rof.readFully(bArr6, i);
                            endArcHeader = new EndArcHeader(baseBlock, bArr6);
                        } else {
                            endArcHeader = new EndArcHeader(baseBlock, (byte[]) null);
                        }
                        this.headers.add(endArcHeader);
                        return;
                    default:
                        byte[] bArr7 = new byte[4];
                        this.rof.readFully(bArr7, 4);
                        BlockHeader blockHeader = new BlockHeader(baseBlock, bArr7);
                        int ordinal = blockHeader.getHeaderType().ordinal();
                        if (ordinal == 1 || ordinal == 2) {
                            int headerSize = (blockHeader.getHeaderSize() - 7) - 4;
                            byte[] bArr8 = new byte[headerSize];
                            this.rof.readFully(bArr8, headerSize);
                            FileHeader fileHeader = new FileHeader(blockHeader, bArr8);
                            this.headers.add(fileHeader);
                            this.rof.setPosition(fileHeader.getPositionInFile() + ((long) fileHeader.getHeaderSize()) + fileHeader.getFullPackSize());
                            break;
                        } else if (ordinal == 3) {
                            int headerSize2 = (blockHeader.getHeaderSize() - 7) - 4;
                            byte[] bArr9 = new byte[headerSize2];
                            this.rof.readFully(bArr9, headerSize2);
                            ProtectHeader protectHeader = new ProtectHeader(blockHeader, bArr9);
                            this.rof.setPosition(protectHeader.getPositionInFile() + ((long) protectHeader.getHeaderSize()) + ((long) protectHeader.getDataSize()));
                            break;
                        } else if (ordinal == 4) {
                            byte[] bArr10 = new byte[3];
                            this.rof.readFully(bArr10, 3);
                            SubBlockHeader subBlockHeader = new SubBlockHeader(blockHeader, bArr10);
                            subBlockHeader.print();
                            int ordinal2 = subBlockHeader.getSubType().ordinal();
                            if (ordinal2 != 1) {
                                if (ordinal2 != 3) {
                                    if (ordinal2 != 6) {
                                        break;
                                    } else {
                                        int headerSize3 = ((subBlockHeader.getHeaderSize() - 7) - 4) - 3;
                                        byte[] bArr11 = new byte[headerSize3];
                                        this.rof.readFully(bArr11, headerSize3);
                                        UnixOwnersHeader unixOwnersHeader = new UnixOwnersHeader(subBlockHeader, bArr11);
                                        unixOwnersHeader.print();
                                        this.headers.add(unixOwnersHeader);
                                        break;
                                    }
                                } else {
                                    byte[] bArr12 = new byte[10];
                                    this.rof.readFully(bArr12, 10);
                                    EAHeader eAHeader = new EAHeader(subBlockHeader, bArr12);
                                    eAHeader.print();
                                    this.headers.add(eAHeader);
                                    break;
                                }
                            } else {
                                byte[] bArr13 = new byte[8];
                                this.rof.readFully(bArr13, 8);
                                MacInfoHeader macInfoHeader = new MacInfoHeader(subBlockHeader, bArr13);
                                macInfoHeader.print();
                                this.headers.add(macInfoHeader);
                                break;
                            }
                        } else {
                            logger.warning("Unknown Header");
                            throw new RarException(RarException.RarExceptionType.notRarArchive);
                        }
                }
            } else {
                return;
            }
        }
    }

    /* renamed from: ir.mahdi.mzip.rar.Archive$2  reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|14) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            int[] iArr = new int[UnrarHeadertype.values().length];
            $SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype = iArr;
            iArr[UnrarHeadertype.MarkHeader.ordinal()] = 1;
            $SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype[UnrarHeadertype.MainHeader.ordinal()] = 2;
            $SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype[UnrarHeadertype.SignHeader.ordinal()] = 3;
            $SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype[UnrarHeadertype.AvHeader.ordinal()] = 4;
            $SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype[UnrarHeadertype.CommHeader.ordinal()] = 5;
            try {
                $SwitchMap$ir$mahdi$mzip$rar$rarfile$UnrarHeadertype[UnrarHeadertype.EndArcHeader.ordinal()] = 6;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    /* renamed from: ir.mahdi.mzip.rar.Archive$AnonymousClass2  reason: case insensitive filesystem */
    public static class C0020AnonymousClass2 {
        static final int[] iArr;

        static {
            int[] iArr2 = new int[UnrarHeadertype.values().length];
            iArr = iArr2;
            try {
                iArr2[UnrarHeadertype.NewSubHeader.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr[UnrarHeadertype.FileHeader.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr[UnrarHeadertype.ProtectHeader.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                iArr[UnrarHeadertype.SubHeader.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                iArr[UnrarHeadertype.MarkHeader.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr3 = iArr;
            iArr3[UnrarHeadertype.MainHeader.ordinal()] = 6;
            iArr3[UnrarHeadertype.SignHeader.ordinal()] = 7;
            iArr3[UnrarHeadertype.AvHeader.ordinal()] = 8;
            iArr3[UnrarHeadertype.CommHeader.ordinal()] = 9;
            try {
                iArr3[UnrarHeadertype.EndArcHeader.ordinal()] = 10;
            } catch (NoSuchFieldError unused6) {
            }
            int[] iArr4 = new int[SubBlockHeaderType.values().length];
            iArr4[SubBlockHeaderType.MAC_HEAD.ordinal()] = 1;
            iArr4[SubBlockHeaderType.BEEA_HEAD.ordinal()] = 2;
            iArr4[SubBlockHeaderType.EA_HEAD.ordinal()] = 3;
            iArr4[SubBlockHeaderType.NTACL_HEAD.ordinal()] = 4;
            iArr4[SubBlockHeaderType.STREAM_HEAD.ordinal()] = 5;
            try {
                iArr4[SubBlockHeaderType.UO_HEAD.ordinal()] = 6;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public void extractFile(FileHeader fileHeader, OutputStream outputStream) throws RarException {
        if (this.headers.contains(fileHeader)) {
            try {
                doExtractFile(fileHeader, outputStream);
            } catch (Exception e) {
                if (e instanceof RarException) {
                    throw ((RarException) e);
                }
                throw new RarException(e);
            }
        } else {
            throw new RarException(RarException.RarExceptionType.headerNotInArchive);
        }
    }

    private void doExtractFile(FileHeader fileHeader, OutputStream outputStream) throws RarException, IOException {
        long j;
        this.dataIO.init(outputStream);
        this.dataIO.init(fileHeader);
        this.dataIO.setUnpFileCRC(isOldFormat() ? 0 : -1);
        if (this.unpack == null) {
            this.unpack = new Unpack(this.dataIO);
        }
        if (!fileHeader.isSolid()) {
            this.unpack.init((byte[]) null);
        }
        this.unpack.setDestSize(fileHeader.getFullUnpackSize());
        try {
            this.unpack.doUnpack(fileHeader.getUnpVersion(), fileHeader.isSolid());
            FileHeader subHeader = this.dataIO.getSubHeader();
            if (subHeader.isSplitAfter()) {
                j = this.dataIO.getPackedCRC();
            } else {
                j = this.dataIO.getUnpFileCRC();
            }
            if ((-1 ^ j) != ((long) subHeader.getFileCRC())) {
                throw new RarException(RarException.RarExceptionType.crcError);
            }
        } catch (Exception e) {
            this.unpack.cleanUp();
            if (e instanceof RarException) {
                throw ((RarException) e);
            }
            throw new RarException(e);
        }
    }

    public MainHeader getMainHeader() {
        return this.newMhd;
    }

    public boolean isOldFormat() {
        return this.markHead.isOldFormat();
    }

    public void close() throws IOException {
        IReadOnlyAccess iReadOnlyAccess = this.rof;
        if (iReadOnlyAccess != null) {
            iReadOnlyAccess.close();
            this.rof = null;
        }
        Unpack unpack2 = this.unpack;
        if (unpack2 != null) {
            unpack2.cleanUp();
        }
    }

    public VolumeManager getVolumeManager() {
        return this.volumeManager;
    }

    public void setVolumeManager(VolumeManager volumeManager2) {
        this.volumeManager = volumeManager2;
    }

    public Volume getVolume() {
        return this.volume;
    }

    public void setVolume(Volume volume2) throws IOException {
        this.volume = volume2;
        setFile(volume2.getReadOnlyAccess(), volume2.getLength());
    }
}
