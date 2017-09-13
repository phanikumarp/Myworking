#!/bin/bash
#
# WAR to DEB
# This script takes a WAR file, and packages it into a DEB file targetting tomcat's default webapps directory,
# /var/lib/tomcatX/webapps
# Arguments:
# -h    Help text
# -s    Tomcat version (default: 7)
# -p    Custom output path
# -f    Input WAR file
# -v    WAR release version (e.g. 1.0 or 1.0.0)
# Dependencies: dh-make debhelper devscripts fakeroot
#
# Debian package construction based on this guide:

tomcat_server="/home/phani/apache-tomcat-7.0.72"
ctx_dir=$(pwd)
base_dir=
app_name="monitoring-services"
app_version=1.0
tomcat_version=7.0.72
tomcat_path=$(get_tomcat_path $tomcat_version)
file_name="monitoring-services"
out_file="monitoring-services"
in_file="/home/phani/monitoring-services.war"
app_creator="phani@gmail.com"
do_help=0


get_tomcat_path() {
    echo "/home/phani/apach-tomcat-7.0.72/webapps"
}
cmd_exists() {
    command -v "$1" >/dev/null 2>&1
}
cleanup() {
    rm -rf $base_dir
    printf "Deleted temp working directory %s\n" $base_dir;
}

while getopts ":c:f:hp:s:v:" opt; do
    case $opt in
        c)
            app_creator="${app_creator}"
            ;;
        f)
            in_file="$(readlink -f "$OPTARG")";
            file_name="$(basename "$OPTARG")";
            app_name="${file_name%.*}"
            ;;
        h)
            do_help=1
            ;;
        p)
            tomcat_path="$OPTARG"
            ;;
        s)
            tomcat_version="$OPTARG"
            tomcat_path="$(get_tomcat_path $tomcat_version)"
            ;;
        v)
            app_version="$OPTARG"
            ;;
        \?)
            echo "Invalid option -$OPTARG" >&2
            printf "\n";
            do_help=1
            ;;
    esac
done

if [ "$do_help" -eq 1 ]; then
cat <<HELPTEXT
This command packages a WAR file into a DEB installer, which is convenient for
feeding into the Netflix AMInator system.
Options:
-h       This help text
-c       Creator e-mail address (required)
-s       Set tomcat version (default: 7)
-p       Override output path entirely (default: /var/lib/tomcatX/webapps)
         Where X is the Tomcat version
-f       The WAR file to operate on
-v       WAR release version (e.g. 1.0 or 1.0.0)
Aptitude dependencies:
           dh-make debhelper devscripts fakeroot
HELPTEXT
exit;
fi
printf "\n";
printf "   App name:         %s\n" $app_name;
printf "   App version:      %s\n" $app_version;
printf "   App creator:      %s\n" $app_creator;
printf "   Tomcat version:   %s\n" $tomcat_version;
printf "   Deb install path: %s\n" $tomcat_path;
printf "   WAR file:         %s\n" "$in_file";
printf "\n";
if [ ! -f "$in_file" ]; then
    printf "Input file not specified or unusable. Aborting.\n";
    exit;
else
    printf "File found: %s\n" "$in_file";
    if cmd_exists dh_make && cmd_exists debuild; then
        # Make a directory for the WAR file
        base_dir=$(mktemp -d);
        # Switch to base directory
        cd $base_dir
        working_dir="${app_name}-$app_version"
        printf "Creating package base directory %s\n" $working_dir
        mkdir $working_dir
        cd $working_dir
        printf "Copying WAR file\n"
        cp "$in_file" "$out_file.war"
        printf "Creating DEB package\n"
        dh_make --indep --createorig -y -e "${app_creator}"
        grep -v makefile debian/rules > debian/rules.tmp
        mv debian/rules.tmp debian/rules
        echo "'$out_file'.war  $tomcat_path" > debian/install
        echo "1.0" > debian/source/format
        # Write preinst script
        # This script will run before the installation process
        # It's purpose is to remove the default Tomcat ROOT webapp,
        # as we are going to overwrite it.
        printf "#!/bin/bash\n\nrm -rf /home/phani/apach-tomcat-7.0.72/webapps/$out_file/\n\n#DEBHELPER#" > debian/preinst
        chmod 755 debian/preinst
        rm debian/*.ex
        rm debian/*.EX
        # Set tomcatX dependency in control file
       # sed -i "s/\$\{misc:Depends\}/home/phani/apache-tomcat-${tomcat_version}/" debian/control
        # Build package
        debuild -us -uc
        # Move it to whatever directory the user was in
        mv ../*.deb "$ctx_dir"
        # Clean up temp directory
        cleanup;
        exit;
    else
        printf "One or more dependencies are missing. Run this command with the -h flag for more information.\n";
        exit;
    fi
fi
