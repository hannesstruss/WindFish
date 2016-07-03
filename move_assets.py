#!/usr/bin/env python

import sys
import os
import re
import shutil

asset_re = re.compile(r'^(.*)-(x*(?:h|m)dpi)\.(png)$')

if __name__ == '__main__':
    basedir = os.path.abspath(sys.argv[1])
    print "Moving assets from", basedir

    assets_dir = os.path.join(basedir, 'sketch_export')
    for f in os.listdir(assets_dir):
        m = asset_re.match(f)
        if m:
            target_filename = '.'.join(m.group(1, 3))
            kind = 'mipmap' if m.group(1) == 'ic_launcher' else 'drawable'
            target_dir_name = '%s-%s' % (kind, m.group(2))
            destination = os.path.join(basedir, 'src', 'main', 'res', target_dir_name)
            if not os.path.exists(destination):
                os.mkdir(destination)
            full_destination = os.path.join(destination, target_filename)
            print '%s -> %s' % (f, full_destination)
            shutil.move(os.path.join(assets_dir, f), full_destination)
        else:
            print '%s doesn\'t match' % f
            
